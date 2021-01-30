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
