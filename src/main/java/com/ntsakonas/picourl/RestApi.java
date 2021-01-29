package com.ntsakonas.picourl;

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


    @PostMapping(path = "/url",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> shortenUrl(@RequestParam Map<String, String> requestParameters) {
        System.out.println("shorten url: " + requestParameters.get("url"));
        // TODO:: use fixed URL until the shortener logic is in place
        String shortUrl = "http://pico.url/ab23Jl";
        return new ResponseEntity<>(shortUrl, HttpStatus.CREATED);
    }

    @GetMapping(value = "/url/{shortUrl}")
    public ResponseEntity<String> expandUrl(String shortUrl) {
        // TODO:: use fixed URL until the expander logic is in place
        String expandedUrl = "http://www.google.com";
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, expandedUrl);
        ResponseEntity<String> response = new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

        return response;
    }
}
