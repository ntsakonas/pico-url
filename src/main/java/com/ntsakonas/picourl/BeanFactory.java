package com.ntsakonas.picourl;

import com.ntsakonas.picourl.core.UrlExpander;
import com.ntsakonas.picourl.core.UrlShortener;
import com.ntsakonas.picourl.repository.DatabaseRepo;
import com.ntsakonas.picourl.repository.ShortenedUrlRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanFactory {

    @Bean
    public ShortenedUrlRepository getUrlRepository(DatabaseRepo dbRepository) {
        return new ShortenedUrlRepository(dbRepository);
    }

    @Bean
    public UrlShortener getUrlShortener(ShortenedUrlRepository repository) {
        return new UrlShortener(repository);
    }

    @Bean
    UrlExpander getUrlExpander(ShortenedUrlRepository repository) {
        return new UrlExpander(repository);
    }
}
