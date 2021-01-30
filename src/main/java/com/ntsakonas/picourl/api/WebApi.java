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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/*
 Handles the expansion of a short URL from the root path.
 For example (assuming that our shortening service runs on http://pico.url/)
 if a user enters in their browser the link http://pico.url/ds7zUba this handler
 will redirect them to the original URL.

 This is the equivalent of calling the REST Api http://pico.url/url/ds7zUba
*/

@RestController
public class WebApi {

    private final RestApi restApi;

    @Autowired
    public WebApi(RestApi restApi) {
        this.restApi = restApi;
    }

    @GetMapping(value = "/{shortUrl}")
    public ResponseEntity<String> expandUrl(@PathVariable("shortUrl") String shortUrl) {
        return restApi.expandUrl(shortUrl);
    }

}
