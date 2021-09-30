package io.jay.springbootwebclientsample.user;

import io.jay.springbootwebclientsample.JsonFileStubReader;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserServiceTests {

    private MockWebServer mockWebServer;
    private UserService userService;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        userService = new UserService(WebClient.builder(), mockWebServer.url("/").toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void test_fetchUser_invokesApi() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse());


        userService.fetchUser(1)
                .subscribe();


        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod(), equalTo("GET"));
        assertThat(recordedRequest.getPath(), equalTo("/users/1"));
    }

    @Test
    void test_fetchUser_returnsUser() throws IOException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(JsonFileStubReader.fileToJson("user.json"))
        );


        Mono<User> userMono = userService.fetchUser(1);


        StepVerifier.create(userMono)
                .assertNext(user -> {
                    assertThat(user.getId(), equalTo(1));
                    assertThat(user.getUsername(), is(notNullValue()));
                    assertThat(user.getEmail(), is(notNullValue()));
                    assertThat(user.getName(), is(notNullValue()));
                })
                .verifyComplete();
    }
}
