package com.ntsakonas.picourl;

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

    // shortUrl->longUrl
    private final Map<String, String> imMemCache = new HashMap<>();
    // longUrl->shortUrl
    private final Map<String, String> imMemReverseCache = new HashMap<>();
    // set of all the hashvalues that have been used
    private final Set<Long> usedHashValues = new HashSet<>();

    private final RepositoryStats stats = new RepositoryStats();


    public Set<Long> getHashValues(List<Long> shortHashValues) {
        // given the input list of hashvalues, return the ones that are in use
        Set<Long> usedValues = new HashSet<>(shortHashValues);
        usedValues.retainAll(usedHashValues);
        stats.lookup();
        return Collections.unmodifiableSet(usedValues);
    }

    public void saveShortenedUrl(String longUrl, String shortUrl, long hashValue) {
        imMemCache.put(shortUrl, longUrl);
        imMemReverseCache.put(longUrl, shortUrl);
        usedHashValues.add(hashValue);
        stats.write();
    }

    // resolve a short url back to the original long url
    public Optional<String> getLongUrl(String shortUrl) {

        Optional<String> longUrl = Optional.ofNullable(imMemCache.get(shortUrl));

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
        Optional<String> shortUrl = Optional.ofNullable(imMemReverseCache.get(longUrl));

        if (shortUrl.isPresent()) stats.hit();
        else stats.miss();

        return shortUrl;
    }

    public void showStats() {
        System.out.println("------------------");
        System.out.println(stats);
        System.out.println("------------------");
    }
}
