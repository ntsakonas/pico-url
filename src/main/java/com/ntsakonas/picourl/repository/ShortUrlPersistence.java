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

import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 Application level interfacing to the persistence layer.
*/
public interface ShortUrlPersistence {

    Set<Long> findByHashIn(List<Long> hashValues);

    Optional<String> findShortUrlByLongUrl(String longUrl);

    Optional<String> findLongUrlByShortUrl(String shortUrl);

    void saveShortenedUrl(String longUrl, String shortUrl, long hashValue);

}
