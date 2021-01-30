package com.ntsakonas.picourl.data;

/*
  retrieve from the DB only the longUrl.
  This is used for mapping a ShortUrl->LongUrl without any unnecessary fields
 */
public interface LongUrlOnly {

    String getLongUrl();
}
