/*
    PicoUrl - Practicing on the design of a URL shortener service.

    Copyright (C) 2021, Nick Tsakonas


    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
