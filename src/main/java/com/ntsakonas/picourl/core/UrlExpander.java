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

package com.ntsakonas.picourl.core;

import com.ntsakonas.picourl.repository.ShortenedUrlRepository;

import java.util.Optional;

/*
  Converts a short URL back to the original long URL via DB lookup.
*/
public class UrlExpander {

    private final ShortenedUrlRepository shortenedUrlRepository;

    public UrlExpander(ShortenedUrlRepository repository) {
        this.shortenedUrlRepository = repository;
    }

    public Optional<String> expandUrl(String shortUrl) {
        return shortenedUrlRepository.getLongUrl(shortUrl);
    }

    public void stats() {
        System.out.println("Stats after expanding url");
        shortenedUrlRepository.showStats();
    }
}
