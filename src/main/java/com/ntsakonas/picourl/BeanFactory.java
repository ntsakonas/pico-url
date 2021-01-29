package com.ntsakonas.picourl;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanFactory {

    @Bean
    public UrlShortener getUrlShortener() {
        return new UrlShortener();
    }

    @Bean
    UrlExpander getUrlExpander() {
        return new UrlExpander();
    }
}
