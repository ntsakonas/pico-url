DROP TABLE IF EXISTS shortened_urls;

CREATE TABLE shortened_urls (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  hash_value BIGINT NOT NULL,
  short_url VARCHAR(10) NOT NULL,
  long_url VARCHAR(100) NOT NULL,
  created_at INT NOT NULL
);