package com.ntsakonas.picourl;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanFactory {

    @Bean
    public ShortenedUrlRepository getUrlRepository() {
        return new ShortenedUrlRepository();
    }

    @Bean
    public UrlShortener getUrlShortener(ShortenedUrlRepository repository) {
        return new UrlShortener(repository);
    }

    @Bean
    UrlExpander getUrlExpander() {
        return new UrlExpander();
    }
}
