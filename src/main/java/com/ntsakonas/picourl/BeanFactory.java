package com.ntsakonas.picourl;

import com.ntsakonas.picourl.core.UrlExpander;
import com.ntsakonas.picourl.core.UrlShortener;
import com.ntsakonas.picourl.repository.DatabasePersistence;
import com.ntsakonas.picourl.repository.InMemCache;
import com.ntsakonas.picourl.repository.ShortUrlPersistence;
import com.ntsakonas.picourl.repository.ShortenedUrlRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Clock;

/*
 Beans used in normal operation
*/

@Component
public class BeanFactory {

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


/*
 Beans overriding for testing
*/

@Profile("test")
@Component
class TestBeanFactory {

    @Bean
    @Primary
    public ShortUrlPersistence getUrlPersistence() {
        return new InMemCache();
    }

    @Bean
    @Primary
    public ShortenedUrlRepository getUrlRepositoryInMem(ShortUrlPersistence persistence) {
        return new ShortenedUrlRepository(persistence);
    }
}