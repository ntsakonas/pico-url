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
  The repository provides the connection of the core to the short URL persistence.
  Upon construction it can be configured to use the DB or an in-mem storage.

*/
public class ShortenedUrlRepository {

    /*
        Simplistic statistics to keep track of DB hit/miss and query/write rate
    */
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
