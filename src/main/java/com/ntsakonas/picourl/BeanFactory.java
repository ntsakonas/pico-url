package com.ntsakonas.picourl;

import com.ntsakonas.picourl.core.UrlExpander;
import com.ntsakonas.picourl.core.UrlShortener;
import com.ntsakonas.picourl.repository.DatabasePersistence;
import com.ntsakonas.picourl.repository.ShortenedUrlRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class BeanFactory {

    // @Bean
    // public ShortenedUrlRepository getUrlRepositoryInMem() {
    //     return new ShortenedUrlRepository(new InMemCache());
    // }

    @Bean
    public ShortenedUrlRepository getUrlRepository(DatabasePersistence databasePersistence) {
        return new ShortenedUrlRepository(databasePersistence);
    }

    @Bean
    public UrlShortener getUrlShortener(ShortenedUrlRepository repository) {
        return new UrlShortener(repository);
    }

    @Bean
    public UrlExpander getUrlExpander(ShortenedUrlRepository repository) {
        return new UrlExpander(repository);
    }

    @Bean
    public Clock getSystemClock() {
        return Clock.systemUTC();
    }
}
