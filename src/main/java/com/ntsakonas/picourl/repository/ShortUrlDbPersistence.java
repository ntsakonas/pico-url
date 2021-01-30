package com.ntsakonas.picourl.repository;

import com.ntsakonas.picourl.data.HashValueOnly;
import com.ntsakonas.picourl.data.LongUrlOnly;
import com.ntsakonas.picourl.data.ShortUrlOnly;
import com.ntsakonas.picourl.data.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/*
 Interface to the database where the information about the shortened urls is saved
 (this is not exposed to the app, but is wrapped and its types unpacked)
 */
@Repository
public interface ShortUrlDbPersistence extends JpaRepository<ShortenedUrl, Long> {

    Set<HashValueOnly> findByHashIn(List<Long> hashValues);

    ShortUrlOnly findShortUrlByLongUrl(String longUrl);

    LongUrlOnly findLongUrlByShortUrl(String shortUrl);

}
