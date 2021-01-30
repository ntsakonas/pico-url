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

package com.ntsakonas.picourl.data;

import lombok.Data;

import javax.persistence.*;

/*
 A full entry of a shortened url in the DB (very rarely we retrieve the whole entry)
 most operations use only specific columns when querying the DB.
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
