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

package com.ntsakonas.picourl.repository;

import com.ntsakonas.picourl.data.HashValueOnly;
import com.ntsakonas.picourl.data.ShortenedUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Clock;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class DatabasePersistenceTest {

    ShortUrlDbPersistence dbPersistence = Mockito.mock(ShortUrlDbPersistence.class);
    Clock clock = Mockito.mock(Clock.class);

    @BeforeEach
    void resetMocks() {
        Mockito.reset(dbPersistence, clock);
    }

    DatabasePersistence getDbInstance() {
        return new DatabasePersistence(dbPersistence, clock);
    }

    /*TESTS FOR RETRIEVING HASHES*/
    @Test
    void retrievingExistingHashesReturnsASet() {
        DatabasePersistence db = getDbInstance();
        List<Long> requestedHashes = new ArrayList<>() {{
            add(1234567890L);
            add(9876543210L);
        }};

        Set<HashValueOnly> listOfHashes = new HashSet<>() {{
            add(() -> 999999999L);
        }};

        Mockito.when(dbPersistence.findByHashIn(any())).thenReturn(listOfHashes);

        Set<Long> hashesFound = db.findByHashIn(requestedHashes);

        // verify that the argument was passes to the db as-is
        ArgumentCaptor<List> requestedHashesCaptor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(dbPersistence).findByHashIn(requestedHashesCaptor.capture());
        List<Long> hashesRequested = requestedHashesCaptor.getValue();
        assertThat(hashesRequested.size()).isEqualTo(2);
        assertThat(hashesRequested).asList().contains(1234567890L, 9876543210L);

        // verify that the results of the db are returned without mixing the input list
        assertThat(hashesFound.size()).isEqualTo(1);
        assertThat(hashesFound.contains(999999999L)).isTrue();

    }

    @Test
    void retrievingNonExistingHashesReturnsEmptySet() {
        DatabasePersistence db = getDbInstance();
        List<Long> requestedHashes = new ArrayList<>() {{
            add(1234567890L);
            add(9876543210L);
        }};

        Set<HashValueOnly> listOfHashes = Collections.EMPTY_SET;

        Mockito.when(dbPersistence.findByHashIn(any())).thenReturn(listOfHashes);

        Set<Long> hashesFound = db.findByHashIn(requestedHashes);

        // verify that the results of the db are returned without mixing the input list
        assertThat(hashesFound.size()).isEqualTo(0);
    }


    /*TESTS FOR MAPPING LONG-->SHORT URL*/
    @Test
    void mappingExistingLongUrlReturnsAShortUrl() {
        DatabasePersistence db = getDbInstance();
        Mockito.when(dbPersistence.findLongUrlByShortUrl(eq("shortUrl"))).thenReturn(() -> "longUrl");

        Optional<String> longUrl = db.findLongUrlByShortUrl("shortUrl");
        assertThat(longUrl.isPresent()).isTrue();
        assertThat(longUrl.get()).isEqualTo("longUrl");
    }

    @Test
    void mappingNonExistingLongUrlReturnEmpty() {
        DatabasePersistence db = getDbInstance();
        Mockito.when(dbPersistence.findLongUrlByShortUrl(eq("shortUrl"))).thenReturn(null);

        Optional<String> longUrl = db.findLongUrlByShortUrl("shortUrl");
        assertThat(longUrl.isPresent()).isFalse();
    }


    /*TESTS FOR MAPPING SHORT-->LONG URL*/
    @Test
    void mappingExistingShortUrlReturnsALongUrl() {
        DatabasePersistence db = getDbInstance();
        Mockito.when(dbPersistence.findShortUrlByLongUrl(eq("longUrl"))).thenReturn(() -> "shortUrl");

        Optional<String> longUrl = db.findShortUrlByLongUrl("longUrl");
        assertThat(longUrl.isPresent()).isTrue();
        assertThat(longUrl.get()).isEqualTo("shortUrl");
    }

    @Test
    void mappingNonExistingShortUrlReturnEmpty() {
        DatabasePersistence db = getDbInstance();
        Mockito.when(dbPersistence.findShortUrlByLongUrl(eq("longUrl"))).thenReturn(null);

        Optional<String> longUrl = db.findShortUrlByLongUrl("longUrl");
        assertThat(longUrl.isPresent()).isFalse();
    }

    /*TESTS FOR SAVING ENTRIES*/
    @Test
    void savingAnEntryAddsCurrentTimestamp() {
        long timeStamp = 1234567890L;
        Mockito.when(clock.millis()).thenReturn(timeStamp);

        DatabasePersistence db = getDbInstance();
        db.saveShortenedUrl("longUrl", "shortUrl", 1234L);

        ArgumentCaptor<ShortenedUrl> entryCaptor = ArgumentCaptor.forClass(ShortenedUrl.class);

        Mockito.verify(dbPersistence).save(entryCaptor.capture());
        ShortenedUrl entry = entryCaptor.getValue();

        assertThat(entry.getLongUrl()).isEqualTo("longUrl");
        assertThat(entry.getShortUrl()).isEqualTo("shortUrl");
        assertThat(entry.getHash()).isEqualTo(1234L);
        assertThat(entry.getCreatedAt()).isEqualTo(timeStamp);
    }

}
