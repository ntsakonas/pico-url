package com.ntsakonas.picourl.api;

import com.ntsakonas.picourl.repository.InMemCache;
import com.ntsakonas.picourl.repository.ShortUrlPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class RestApiTest {

    @Autowired
    MockMvc mockHost;

    // during testing, the in-mem storage will be injected
    @Autowired
    ShortUrlPersistence urlPersistence;

    @Value("${picoshortener.host.domain}")
    String serviceDomainName;

    @BeforeEach
    void cleanUpInMemPeristence() {
        // the ShortUrlPersistence instance provided has class lifecycle and should be reset between tests
        // our configuration is such that an in-mem implementation is provided
        if (urlPersistence instanceof InMemCache) {
            ((InMemCache) urlPersistence).reset();
        } else {
            throw new RuntimeException("Something is wrong in the test configuration. An in-mem instance of ShortUrlPersistence should be provided");
        }
    }

    @Test
    public void testApiResponseOnURLExpansion() throws Exception {
        // add the shortened url mapping
        urlPersistence.saveShortenedUrl("http://www.google.com", "2DxAzfF5", 0);

        mockHost.perform(get("/url/2DxAzfF5"))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", "http://www.google.com"));
    }

    @Test
    public void whenShortUrlIsnotFoundError404IsReturned() throws Exception {
        // add the shortened url mapping
        urlPersistence.saveShortenedUrl("http://www.google.com", "2DxAzfF5", 0);

        // request a similar, but not the same url
        mockHost.perform(get("/url/2DxAzf"))
                .andExpect(status().isNotFound());

        // request a non-existent URL
        mockHost.perform(get("/url/lalalala"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testApiResponseOnURLShortening() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("url", "http://www.google.com");
        mockHost.perform(post("/url").contentType("application/x-www-form-urlencoded").params(params))
                .andExpect(status().isCreated())
                .andExpect(content().string(is(serviceDomainName + "2DxAzfF5")));
    }

    @Test
    public void veryLongUrlFailsOnShortening() throws Exception {
        // the service accepts URL up to 150 characters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // this URL is 150 chars (OK)
        params.add("url", "http://www.this-is-a-long-url-that-is-made-up-just-to-consume-enough-space-to-reach-near-the-limit-of-the-shortening-service-and-see-it-that-fails.com");
        mockHost.perform(post("/url").contentType("application/x-www-form-urlencoded").params(params))
                .andExpect(status().isCreated())
                .andExpect(content().string(is(serviceDomainName + "18cJVu3G")));

        // this URL is 151 chars (NOT OK)
        params.remove("url");
        params.add("url", "https://www.this-is-a-long-url-that-is-made-up-just-to-consume-enough-space-to-reach-near-the-limit-of-the-shortening-service-and-see-it-that-fails.com");
        mockHost.perform(post("/url").contentType("application/x-www-form-urlencoded").params(params))
                .andExpect(status().isBadRequest());
    }
}

