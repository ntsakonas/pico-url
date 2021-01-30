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

package com.ntsakonas.picourl.core;

import com.ntsakonas.picourl.repository.ShortenedUrlRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class UrlShortenerTest {

    @Test
    void testShorteningWithoutConflict() {

        ShortenedUrlRepository urlRepository = repoNoMatch();
        UrlShortener shortener = new UrlShortener(urlRepository);

        Optional<String> s = shortener.shortenUrl("http://www.google.com");
        assertThat(s.isPresent()).isTrue();
        assertThat(s.get()).isEqualTo("2DxAzfF5");
    }

    @Test
    void testShorteningWithMainSubHashConflict() {
        // the main hash is already used, it should pick the next one
        Set<Long> usedValues = new HashSet<>() {{
            add(7835846572003L);
        }};

        ShortenedUrlRepository urlRepository = repoWithMatch(usedValues);
        UrlShortener shortener = new UrlShortener(urlRepository);

        Optional<String> s = shortener.shortenUrl("http://www.google.com");
        assertThat(s.isPresent()).isTrue();
        assertThat(s.get()).isEqualTo("8EJKS7f");
    }

    @Test
    void testShorteningWithFirstTwoSubHashesConflict() {

        // both the first and second sub-hash is taken, it shoudl pick the third one
        Set<Long> usedValues = new HashSet<>() {{
            add(7835846572003L);
            add(467513369371L);
        }};

        ShortenedUrlRepository urlRepository = repoWithMatch(usedValues);
        UrlShortener shortener = new UrlShortener(urlRepository);

        Optional<String> s = shortener.shortenUrl("http://www.google.com");
        assertThat(s.isPresent()).isTrue();
        assertThat(s.get()).isEqualTo("40lrRBQx");
    }


    @Test
    void testShorteningWithAllSubHashesOfOriginalHashInConflict() {
        // all the sub-hashes of the original hash are taken
        // we retry by appending an extra value at the URL

        Set<Long> usedValues = allValuesOfOriginalHash();

        ShortenedUrlRepository urlRepository = repoWithMatch(usedValues);
        UrlShortener shortener = new UrlShortener(urlRepository);

        Optional<String> s = shortener.shortenUrl("http://www.google.com");
        assertThat(s.isPresent()).isTrue();
        assertThat(s.get()).isEqualTo("4tx7jWdV");
    }

    @Test
    void testShorteningWithAllSubHashesConflict() {
        // all the sub-hashes of the original hash are taken
        // we retry by appending an extra value at the URL
        // those are taken too
        // we give up

        Set<Long> usedValues = allValuesOfOriginalHash();
        usedValues.addAll(allValuesOfExtendedHash());

        ShortenedUrlRepository urlRepository = repoWithMatch(usedValues);
        UrlShortener shortener = new UrlShortener(urlRepository);

        Optional<String> s = shortener.shortenUrl("http://www.google.com");
        assertThat(s.isPresent()).isFalse();

    }

    private Set<Long> allValuesOfOriginalHash() {
        // all the subhashes from hashing "http://www.google.com"
        return new HashSet<>() {{
            add(7835846572003L);
            add(467513369371L);
            add(14130306292555L);
            add(10960271788938L);
            add(8671996906021L);
            add(3415766345141L);
            add(12419068179894L);
            add(12687966058049L);
            add(11157078688173L);
            add(6278004976943L);
            add(6280344055561L);
            add(6879148181848L);
            add(1843330111549L);
            add(14495671401958L);
            add(16532809574014L);
            add(10274600287976L);
            add(9061953103903L);
            add(15283622780888L);
            add(7142130047212L);
            add(16390129511531L);
            add(8932876380964L);
            add(17424353797195L);
            add(9811502844701L);
            add(13654309936623L);
            add(12250506981373L);
            add(4720671325564L);
            add(12223208324139L);
            add(15324401118071L);
            add(17581384365975L);
            add(14826956363525L);
            add(13380829513106L);
            add(12608262738599L);
            add(8345214953226L);
            add(7720516651537L);
            add(6127425819097L);
            add(2916451735940L);
            add(7739830535296L);
            add(11071780061395L);
            add(2033742566213L);
            add(10464701662571L);
            add(4951346867014L);
            add(907402757830L);
            add(3596687427201L);
            add(5958307054047L);
            add(12398606016417L);
            add(7449652208031L);
            add(7154872459217L);
            add(2060000940460L);
            add(17186845469840L);
            add(1785929175077L);
            add(17393217709476L);
            add(1840664388799L);
            add(13813246377956L);
        }};
    }


    private Set<Long> allValuesOfExtendedHash() {
        // all the subhashes from hashing "http://www.google.com" + UrlShortener.RETRY_SUFFIX
        // -> "http://www.google.comZ&Ks5P}K+-'#[2d<gx#Ma)zccj.@C&u

        return new HashSet<>() {{
            add(17264637503609L);
            add(4108503775566L);
            add(13837989924545L);
            add(6496025755985L);
            add(9317105357279L);
            add(10233855467395L);
            add(16223465079563L);
            add(1451153886164L);
            add(2059487925381L);
            add(17055513609603L);
            add(3349345043250L);
            add(13007400940222L);
            add(4971478302256L);
            add(6061050179838L);
            add(3516474130033L);
            add(3015889023348L);
            add(15603590067320L);
            add(1092825151617L);
            add(15880448147790L);
            add(1599749574251L);
            add(4915611986840L);
            add(9351459477573L);
            add(1436324218270L);
            add(15855278988980L);
            add(12748630963324L);
            add(9095108394025L);
            add(6179191007663L);
            add(16168340008905L);
            add(4931321841997L);
            add(13373182397842L);
            add(10650601231031L);
            add(17357264303934L);
            add(10228778614324L);
            add(14923790693395L);
            add(2986045871074L);
            add(7963743085122L);
            add(15616834683604L);
            add(4483446920221L);
            add(4270318689783L);
            add(2486049830888L);
            add(3110059108551L);
            add(4526759790503L);
            add(15358413481855L);
            add(8696363450133L);
            add(9653601637760L);
            add(8415973048364L);
            add(8242402962457L);
            add(16585019103598L);
            add(6048053816883L);
            add(189405213653L);
            add(13303362606548L);
            add(10368920704168L);
            add(15615793604776L);
        }};
    }


    private ShortenedUrlRepository repoNoMatch() {
        ShortenedUrlRepository urlRepository = Mockito.mock(ShortenedUrlRepository.class);
        Mockito.when(urlRepository.getHashValues(any())).thenReturn(Collections.emptySet());
        return urlRepository;
    }

    private ShortenedUrlRepository repoWithMatch(Set<Long> matches) {
        ShortenedUrlRepository urlRepository = Mockito.mock(ShortenedUrlRepository.class);
        Mockito.when(urlRepository.getHashValues(any())).thenReturn(matches);
        return urlRepository;
    }

}
