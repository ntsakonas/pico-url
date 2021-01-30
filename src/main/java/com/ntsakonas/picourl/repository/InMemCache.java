package com.ntsakonas.picourl.repository;

import java.util.*;

/*
   This can be used for in-memory storage (useful for testing)
   (the in-memory cache does not support expiration)
 */
public class InMemCache implements ShortUrlPersistence {

    // shortUrl->longUrl
    private final Map<String, String> inMemCache = new HashMap<>();
    // longUrl->shortUrl
    private final Map<String, String> inMemReverseCache = new HashMap<>();
    // set of all the hashvalues that have been used
    private final Set<Long> usedHashValues = new HashSet<>();

    @Override
    public Set<Long> findByHashIn(List<Long> hashValues) {
        return Collections.unmodifiableSet(usedHashValues);
    }

    @Override
    public Optional<String> findShortUrlByLongUrl(String longUrl) {
        return Optional.ofNullable(inMemReverseCache.get(longUrl));
    }

    @Override
    public Optional<String> findLongUrlByShortUrl(String shortUrl) {
        return Optional.ofNullable(inMemCache.get(shortUrl));
    }

    @Override
    public void saveShortenedUrl(String longUrl, String shortUrl, long hashValue) {
        inMemCache.put(shortUrl, longUrl);
        inMemReverseCache.put(longUrl, shortUrl);
        usedHashValues.add(hashValue);
    }

    /*
     not part of the ShortUrlPersistence API, used in tests to clean up at will
    */
    public void reset() {
        inMemCache.clear();
        inMemReverseCache.clear();
        usedHashValues.clear();
    }
}
