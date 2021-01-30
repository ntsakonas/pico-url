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
