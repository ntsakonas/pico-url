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