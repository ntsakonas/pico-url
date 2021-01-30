package com.ntsakonas.picourl.repository;

import java.util.*;

public class ShortenedUrlRepository {

    private static class RepositoryStats {

        int numOfLookups = 0;
        int numOfHits = 0;
        int numOfMisses = 0;
        int numOfWrites = 0;

        void lookup() {
            numOfLookups++;
        }

        void write() {
            numOfWrites++;
        }

        void miss() {
            numOfMisses++;
        }

        void hit() {
            numOfHits++;
        }

        @Override
        public String toString() {
            return "RepositoryStats: " +
                    " numOfLookups=" + numOfLookups +
                    " numOfHits=" + numOfHits +
                    " numOfMisses=" + numOfMisses +
                    " numOfWrites=" + numOfWrites;
        }
    }


    private final RepositoryStats stats = new RepositoryStats();

    private final ShortUrlPersistence shortUrlPersistence;


    public ShortenedUrlRepository(ShortUrlPersistence shortUrlPersistence) {
        this.shortUrlPersistence = shortUrlPersistence;
    }

    public Set<Long> getHashValues(List<Long> hashValues) {
        // given the input list of hash values, return the ones that are in use
        Set<Long> usedHashValues = shortUrlPersistence.findByHashIn(hashValues);
        stats.lookup();
        Set<Long> usedValues = new HashSet<>(hashValues);
        usedValues.retainAll(usedHashValues);
        return Collections.unmodifiableSet(usedValues);
    }

    public void saveShortenedUrl(String longUrl, String shortUrl, long hashValue) {
        shortUrlPersistence.saveShortenedUrl(longUrl, shortUrl, hashValue);
        stats.write();
    }

    // resolve a short url back to the original long url
    public Optional<String> getLongUrl(String shortUrl) {
        Optional<String> longUrl = shortUrlPersistence.findLongUrlByShortUrl(shortUrl);

        if (longUrl.isPresent()) stats.hit();
        else stats.miss();

        return longUrl;
    }

    // get the short url associated with a long url
    // used for pre-checking, if the long url is already shortened
    // avoid doing it again. The conflict in the hashing will force the long url
    // to be mapped to a different short url, wasting space
    // at the expense of having another db read we can save space.
    public Optional<String> getShortUrl(String longUrl) {
        Optional<String> shortUrl = shortUrlPersistence.findShortUrlByLongUrl(longUrl);

        if (shortUrl.isPresent()) stats.hit();
        else stats.miss();

        return shortUrl;
    }

    public void showStats() {
        System.out.println("----------------------");
        System.out.println(stats);
        System.out.println("----------------------");
    }
}
