package com.ntsakonas.picourl.api;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class WebApiTest {

    private ResponseEntity<String> validResponse() {
        // the contents of the response do not have to be meaningful
        // as long as we can verify that the webapi does not interfere with that
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "http://go.here/");
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Test
    void verifyThatGlobalHandlerRedirectsToTheRestApi() {
        RestApi restApiMock = Mockito.mock(RestApi.class);

        ResponseEntity<String> validResponse = validResponse();
        Mockito.when(restApiMock.expandUrl(anyString())).thenReturn(validResponse);
        String shortUrl = "shorturl";

        WebApi webApi = new WebApi(restApiMock);
        ResponseEntity<String> response = webApi.expandUrl(shortUrl);

        // verify that the argument was passed through to the rest api
        ArgumentCaptor<String> shortUrlCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(restApiMock).expandUrl(shortUrlCaptor.capture());
        assertThat(shortUrlCaptor.getValue()).isEqualTo(shortUrl);

        // verify that the response is returned as-is
        assertThat(response).isEqualTo(validResponse);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY);
        assertThat(response.getHeaders()).isNotNull();
        assertThat(response.getHeaders().size()).isEqualTo(1);
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION).size()).isEqualTo(1);
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION).get(0)).isEqualTo("http://go.here/");

    }
}
