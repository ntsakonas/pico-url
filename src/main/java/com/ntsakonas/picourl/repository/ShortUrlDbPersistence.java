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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/*
 Interface to the database where the information about the shortened urls is saved
 (this is not exposed to the app, but is wrapped and its types unpacked)
 */
@Repository
public interface ShortUrlDbPersistence extends JpaRepository<ShortenedUrl, Long> {

    Set<HashValueOnly> findByHashIn(List<Long> hashValues);

    ShortUrlOnly findShortUrlByLongUrl(String longUrl);

    LongUrlOnly findLongUrlByShortUrl(String shortUrl);

}
