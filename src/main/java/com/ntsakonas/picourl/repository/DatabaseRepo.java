package com.ntsakonas.picourl.repository;

import com.ntsakonas.picourl.data.HashValueOnly;
import com.ntsakonas.picourl.data.LongUrlOnly;
import com.ntsakonas.picourl.data.ShortUrlOnly;
import com.ntsakonas.picourl.data.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 Interface to the database where the information about the shortened urls is saved
 */
@Repository
public interface DatabaseRepo extends JpaRepository<ShortenedUrl, Long> {

    List<HashValueOnly> findByHashIn(List<Long> hashValues);

    ShortUrlOnly findShortUrlByLongUrl(String longUrl);

    LongUrlOnly findLongUrlByShortUrl(String shortUrl);

}
