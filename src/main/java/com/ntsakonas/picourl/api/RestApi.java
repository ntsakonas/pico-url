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

package com.ntsakonas.picourl.api;

import com.ntsakonas.picourl.core.UrlExpander;
import com.ntsakonas.picourl.core.UrlShortener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/*
 Rest API to access the URL shortener.
 NOTE: the shortening assumes that the owner has a registered domain that
 is used in the returned URL. For this implementation an unregistered URL is used (http://pico.url/).
 */
@RestController
public class RestApi {

    private final int MAX_URL_LENGTH = 150;

    private final UrlShortener urlShortener;
    private final UrlExpander urlExpander;
    private final String SERVICE_URL;

    @Autowired
    public RestApi(UrlShortener urlShortener, UrlExpander urlExpander, @Value("${picoshortener.host.domain}") String serviceDomainName) {
        this.urlShortener = urlShortener;
        this.urlExpander = urlExpander;
        this.SERVICE_URL = serviceDomainName;
    }

    @PostMapping(path = "/url", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> shortenUrl(@RequestParam Map<String, String> requestParameters) {
        String longUrl = requestParameters.get("url");
        if (longUrl == null || longUrl.length() > MAX_URL_LENGTH || longUrl.isEmpty() || longUrl.isBlank())
            return new ResponseEntity<>(String.format("Sorry, we can only shorten URLs up to %d characters long", MAX_URL_LENGTH), HttpStatus.BAD_REQUEST);

        Optional<String> shortUrl = urlShortener.shortenUrl(longUrl);
        urlShortener.stats();
        if (shortUrl.isPresent())
            return new ResponseEntity<>(SERVICE_URL + shortUrl.get(), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/url/{shortUrl}")
    public ResponseEntity<String> expandUrl(@PathVariable("shortUrl") String shortUrl) {
        Optional<String> expandedUrl = urlExpander.expandUrl(shortUrl);
        urlExpander.stats();
        return expandedUrl.map((Function<String, ResponseEntity<String>>) url -> {
            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, url);
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
