package com.ntsakonas.picourl.data;


/*
  retrieve from the DB only the hashvalue associated with the shortUrl.
  This is used for conflict resolution when mapping a LongUrl->ShortUrl without any unnecessary fields
 */
public interface HashValueOnly {

    Long getHash();
}
