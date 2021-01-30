package com.ntsakonas.picourl.repository;

import com.ntsakonas.picourl.data.HashValueOnly;
import com.ntsakonas.picourl.data.LongUrlOnly;
import com.ntsakonas.picourl.data.ShortUrlOnly;
import com.ntsakonas.picourl.data.ShortenedUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/*
 Interface to the database where the information about the shortened URLs is saved
 */
@Component
public class DatabasePersistence implements ShortUrlPersistence {

    private final ShortUrlDbPersistence dbPersistence;
    private final Clock clock;

    @Autowired
    DatabasePersistence(ShortUrlDbPersistence dbPersistence, Clock clock) {
        this.dbPersistence = dbPersistence;
        this.clock = clock;
    }

    @Override
    public Set<Long> findByHashIn(List<Long> hashValues) {
        // not using the cache or not found in cache, check the db
        Set<HashValueOnly> hashes = dbPersistence.findByHashIn(hashValues);

        System.out.println("******* DB hashes*****");
        hashes.stream().forEach(s -> System.out.println(s.getHash()));
        System.out.println("------------------");

        return hashes.stream().map(s -> s.getHash()).collect(Collectors.toSet());
    }

    @Override
    public Optional<String> findShortUrlByLongUrl(String longUrl) {
        ShortUrlOnly shortUrl = dbPersistence.findShortUrlByLongUrl(longUrl);

        System.out.println("******* DB long->short*****");
        System.out.println("[" + longUrl + "]->[" + (shortUrl != null ? shortUrl.getShortUrl() : "n/a") + "]");
        System.out.println("------------------");

        return Optional.ofNullable((shortUrl != null ? shortUrl.getShortUrl() : null));
    }

    @Override
    public Optional<String> findLongUrlByShortUrl(String shortUrl) {
        LongUrlOnly longUrl = dbPersistence.findLongUrlByShortUrl(shortUrl);

        System.out.println("******* DB short->long*****");
        System.out.println("[" + shortUrl + "]->[" + (longUrl != null ? longUrl.getLongUrl() : "n/a") + "]");
        System.out.println("------------------");

        return Optional.ofNullable((longUrl != null ? longUrl.getLongUrl() : null));
    }

    @Override
    public void saveShortenedUrl(String longUrl, String shortUrl, long hashValue) {
        dbPersistence.save(new ShortenedUrl(hashValue, shortUrl, longUrl, clock.millis()));
    }

}
