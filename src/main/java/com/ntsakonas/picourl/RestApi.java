package com.ntsakonas.picourl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RestApi {

    private final UrlShortener urlShortener;
    private final UrlExpander urlExpander;

    @Autowired
    public RestApi(UrlShortener urlShortener, UrlExpander urlExpander) {
        this.urlShortener = urlShortener;
        this.urlExpander = urlExpander;
    }

    @PostMapping(path = "/url",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> shortenUrl(@RequestParam Map<String, String> requestParameters) {
        String shortUrl = urlShortener.shortenUrl(requestParameters.get("url"));
        return new ResponseEntity<>(shortUrl, HttpStatus.CREATED);
    }

    @GetMapping(value = "/url/{shortUrl}")
    public ResponseEntity<String> expandUrl(String shortUrl) {
        String expandedUrl = urlExpander.expandUrl(shortUrl);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, expandedUrl);
        ResponseEntity<String> response = new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

        return response;
    }


}
