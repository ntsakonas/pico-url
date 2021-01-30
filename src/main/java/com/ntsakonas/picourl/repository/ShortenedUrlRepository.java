package com.ntsakonas.picourl.repository;

import com.ntsakonas.picourl.data.HashValueOnly;
import com.ntsakonas.picourl.data.LongUrlOnly;
import com.ntsakonas.picourl.data.ShortUrlOnly;
import com.ntsakonas.picourl.data.ShortenedUrl;

import java.util.*;
import java.util.stream.Collectors;

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

    // This can be used for short-term in-memory cache

    // shortUrl->longUrl
    private final Map<String, String> imMemCache = new HashMap<>();
    // longUrl->shortUrl
    private final Map<String, String> imMemReverseCache = new HashMap<>();
    // set of all the hashvalues that have been used
    private final Set<Long> usedHashValues = new HashSet<>();

    private final RepositoryStats inMemCacheStats = new RepositoryStats();
    private final RepositoryStats dbStats = new RepositoryStats();


    private final DatabaseRepo dbRepository;

    private final boolean USING_IN_MEM_CACHE = true;

    public ShortenedUrlRepository(DatabaseRepo dbRepository) {
        this.dbRepository = dbRepository;
    }

    public Set<Long> getHashValues(List<Long> shortHashValues) {
        // given the input list of hash values, return the ones that are in use
        if (USING_IN_MEM_CACHE) {
            Set<Long> usedValues = new HashSet<>(shortHashValues);
            usedValues.retainAll(usedHashValues);
            inMemCacheStats.lookup();
            if (!usedValues.isEmpty())
                return Collections.unmodifiableSet(usedValues);
        }
        // not using the cache or not found in cache, check the db
        Set<HashValueOnly> hashes = dbRepository.findByHashIn(shortHashValues);
        dbStats.lookup();

        System.out.println("******* DB hashes*****");
        hashes.stream().forEach(s -> System.out.println(s.getHash()));
        System.out.println("------------------");

        return hashes.stream().map(s -> s.getHash()).collect(Collectors.toSet());

    }

    public void saveShortenedUrl(String longUrl, String shortUrl, long hashValue) {
        if (USING_IN_MEM_CACHE) {
            imMemCache.put(shortUrl, longUrl);
            imMemReverseCache.put(longUrl, shortUrl);
            usedHashValues.add(hashValue);
            inMemCacheStats.write();
        }

        ShortenedUrl entry = new ShortenedUrl(hashValue, shortUrl, longUrl, 0L);
        dbRepository.save(entry);
        dbStats.write();
    }

    // resolve a short url back to the original long url
    public Optional<String> getLongUrl(String shortUrl) {
        if (USING_IN_MEM_CACHE) {
            Optional<String> longUrl = Optional.ofNullable(imMemCache.get(shortUrl));

            if (longUrl.isPresent()) {
                inMemCacheStats.hit();
                return longUrl;
            } else inMemCacheStats.miss();
        }

        LongUrlOnly longUrl = dbRepository.findLongUrlByShortUrl(shortUrl);
        System.out.println("******* DB short->long*****");
        System.out.println("[" + shortUrl + "]->[" + (longUrl != null ? longUrl.getLongUrl() : "n/a") + "]");
        System.out.println("------------------");

        if (longUrl == null) {
            dbStats.miss();
            return Optional.empty();
        }

        Optional<String> dbResult = Optional.ofNullable(longUrl.getLongUrl());
        if (dbResult.isPresent()) dbStats.hit();
        else dbStats.miss();

        return dbResult;
    }

    // get the short url associated with a long url
    // used for pre-checking, if the long url is already shortened
    // avoid doing it again. The conflict in the hashing will force the long url
    // to be mapped to a different short url, wasting space
    // at the expense of having another db read we can save space.
    public Optional<String> getShortUrl(String longUrl) {
        if (USING_IN_MEM_CACHE) {
            Optional<String> shortUrl = Optional.ofNullable(imMemReverseCache.get(longUrl));

            if (shortUrl.isPresent()) {
                inMemCacheStats.hit();
                return shortUrl;
            } else inMemCacheStats.miss();
        }
        ShortUrlOnly shortUrl = dbRepository.findShortUrlByLongUrl(longUrl);
        System.out.println("******* DB long->short*****");
        System.out.println("[" + longUrl + "]->[" + (shortUrl != null ? shortUrl.getShortUrl() : "n/a") + "]");
        System.out.println("------------------");

        if (shortUrl == null) {
            dbStats.miss();
            return Optional.empty();
        }
        Optional<String> dbResult = Optional.ofNullable(shortUrl.getShortUrl());
        if (dbResult.isPresent()) dbStats.hit();
        else dbStats.miss();
        return dbResult;
    }

    public void showStats() {
        if (USING_IN_MEM_CACHE) {
            System.out.println("-----IN-MEM-CACHE-----");
            System.out.println(inMemCacheStats);
            System.out.println("----------------------");
        }
        System.out.println("----------DB----------");
        System.out.println(dbStats);
        System.out.println("----------------------");
    }


}
