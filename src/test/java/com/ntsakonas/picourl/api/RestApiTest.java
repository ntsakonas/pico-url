package com.ntsakonas.picourl.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class RestApiTest {

    @Autowired
    MockMvc mockHost;

    @Test
    public void testApiResponseOnURLExpansion() throws Exception {
        mockHost.perform(get("/url/2DxAzfF5"))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", "http://www.google.com"));
    }

    @Test
    public void testApiResponseOnURLShortening() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("url", "http://www.google.com");
        mockHost.perform(post("/url").contentType("application/x-www-form-urlencoded").params(params))
                .andExpect(status().isCreated())
                .andExpect(content().string(is("http://pico.url/2DxAzfF5")));
    }
}

