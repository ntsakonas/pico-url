package com.ntsakonas.picourl.data;

/*
  retrieve from the DB only the shortUrl.
  This is used for mapping a LongUrl->ShortUrl without any unnecessary fields
 */
public interface ShortUrlOnly {

    String getShortUrl();
}
