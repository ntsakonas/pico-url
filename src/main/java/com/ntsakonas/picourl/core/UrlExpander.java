package com.ntsakonas.picourl.core;

import com.ntsakonas.picourl.repository.ShortenedUrlRepository;

import java.util.Optional;

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
