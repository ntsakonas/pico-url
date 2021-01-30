package com.ntsakonas.picourl.core;

import com.ntsakonas.picourl.repository.ShortenedUrlRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ntsakonas.picourl.core.HashingHelper.bytesToHexString;
import static com.ntsakonas.picourl.core.HashingHelper.stringToBytes;

/*
 Converts a long URL (up to 150 characters) to a short form (up to 8 characters).
 The shortening is guaranteed to either be unique or fail.

 No 2 short URLs map to the same long URL and no 2 long URLs map to the same short URL.
*/
public class UrlShortener {

    private static class HashPair {

        final Long hashValue;
        final String shortUrl;

        private HashPair(Long hashValue, String shortUrl) {
            this.hashValue = hashValue;
            this.shortUrl = shortUrl;
        }
    }

    private final static String RETRY_SUFFIX = "Z&Ks5P}K+-'#[2d<gx#Ma)zccj.@C&u";
    private final int SHORT_URL_LENGTH = 8;

    private final String DIGEST_ALGORITHM = "SHA-512";
    private final int SIGNIFICANT_HASH_NIBBLES = 11;
    private final int NUM_OF_STRIDES = 64 - 11;

    private final ShortenedUrlRepository shortenedUrlRepository;

    public UrlShortener(ShortenedUrlRepository shortenedUrlRepository) {
        this.shortenedUrlRepository = shortenedUrlRepository;
    }

    public Optional<String> shortenUrl(String longUrl) {
        // check whether the long url is already shortened
        Optional<String> alreadyShortenedUrl = shortenedUrlRepository.getShortUrl(longUrl);
        return alreadyShortenedUrl.or(() -> {

            Optional<HashPair> shortUrlPair = shortenUrlWithRetry(longUrl);
            shortUrlPair.ifPresent(hashPair -> shortenedUrlRepository.saveShortenedUrl(longUrl, hashPair.shortUrl, hashPair.hashValue));

            return shortUrlPair.flatMap(hashPair -> Optional.of(hashPair.shortUrl));
        });
    }

    private Optional<HashPair> shortenUrlWithRetry(String longUrl) {
        // attempt to shorten the url, if it fails then append a random string and try again
        return createShortUrl(longUrl)
                .or(() -> createShortUrl(longUrl + RETRY_SUFFIX));
    }

    private Optional<HashPair> createShortUrl(String longUrl) {
        try {
            String urlHash = hashUrl(longUrl);
            // it would be enough to get the first 5.5 bytes of the hash and map it to the short url
            // but in case we have a collision we need to attempt to remap it
            // splitting the hash into smaller sub-hashes of 5.5 bytes we have the alternatives at hand.
            // if the first it taken we find the first of the alternatives that is not.
            //
            // the optimistic way to see this approach is that the first subhash will do the job, if not
            // then we try one of the sub-hashes. we may encounter the case where the subhash is actually
            // the first part of the hash of another entry
            //
            List<Long> shortHashValues = expandHash(urlHash);

            // ideally the first part will be the result...but let's check for collisions
            Long hashValue = shortHashValues.get(0);

            // query the repository for the hashValues
            // not all of them will be returned (ideally none of them)
            Set<Long> usedValues = shortenedUrlRepository.getHashValues(shortHashValues);
            if (usedValues.isEmpty()) {
                // none of the sub-hashes is used, easy case, we use the first part of the calculated hash value
                return Optional.of(new HashPair(hashValue, base62Encode(hashValue)));
            } else {
                shortHashValues.removeAll(usedValues);
                if (shortHashValues.isEmpty()) {
                    // the worst case scenario..all the subhashes are already used
                    // we need to re-hash the input url (adding some random data)
                    return Optional.empty();
                }
                if (shortHashValues.contains(hashValue)) {
                    // if the first part is unused, prefer that
                    return Optional.of(new HashPair(hashValue, base62Encode(hashValue)));
                } else {
                    //otherwise, anything works
                    return Optional.of(new HashPair(shortHashValues.get(0), base62Encode(shortHashValues.get(0))));
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private String base62Encode(Long hashValue) {
        // the input value is made up of 11 nibbles.
        // The resulting number needs 8 base-62 digits to be represented
        return Base62Encoder.base62Encode(hashValue.longValue(), SHORT_URL_LENGTH);
    }

    private List<Long> expandHash(String urlHash) {
        /*
          the hashValue is 64 bytes long
          using 5.5 bytes (11 nibbles) (from 0x0 - 0xfffffffffff) we can reach the value of   17,592,186,044,415 (17 trillion)
          using 6 bytes (12 nibbles) (from 0x0 - 0xffffffffffff) we can reach the value of 281,474,976,710,655 (281 trillion)
         */
        List<Long> shortHashValues = new ArrayList<>();
        for (int start = 0; start < NUM_OF_STRIDES; start++) {
            shortHashValues.add(Long.parseUnsignedLong(urlHash.substring(2 * start, 2 * start + SIGNIFICANT_HASH_NIBBLES), 16));
        }
        return shortHashValues;
    }

    private String hashUrl(String longUrl) throws NoSuchAlgorithmException {
        return bytesToHexString(MessageDigest.getInstance(DIGEST_ALGORITHM).digest(stringToBytes(longUrl)));
    }

    public void stats() {
        System.out.println("Stats after shortening url");
        shortenedUrlRepository.showStats();
    }
}
