package com.ntsakonas.picourl;

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

        // TODO:: the url expander is not in place,at the moment a hardwired url is returned
        mockHost.perform(get("/url/dummy"))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", "http://www.google.com"));
    }

    @Test
    public void testApiResponseOnURLShortening() throws Exception {

        // TODO:: the url shortener is not in place,at the moment a hardwired url is returned
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("url", "http://www.google.com");
        mockHost.perform(post("/url").contentType("application/x-www-form-urlencoded").params(params))
                .andExpect(status().isCreated())
                .andExpect(content().string(is("http://pico.url/ab23Jl")));
    }
}

