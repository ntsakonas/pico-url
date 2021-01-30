package com.ntsakonas.picourl.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ShortUrlPersistence {

    Set<Long> findByHashIn(List<Long> hashValues);

    Optional<String> findShortUrlByLongUrl(String longUrl);

    Optional<String> findLongUrlByShortUrl(String shortUrl);

    void saveShortenedUrl(String longUrl, String shortUrl, long hashValue);

}
