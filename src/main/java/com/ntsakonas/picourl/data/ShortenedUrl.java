package com.ntsakonas.picourl.data;

import lombok.Data;

import javax.persistence.*;

/*
 A full entry of a shortened url
 */

@Entity
@Table(name = "shortened_urls")
@Data
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "hash_value")
    private Long hash;

    @Column(name = "short_url", length = 10)
    private String shortUrl;

    @Column(name = "long_url", length = 100)
    private String longUrl;

    @Column(name = "created_at")
    private Long createdAt;

    public ShortenedUrl(Long hash, String shortUrl, String longUrl, Long createdAt) {
        this.hash = hash;
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.createdAt = createdAt;
    }
}
