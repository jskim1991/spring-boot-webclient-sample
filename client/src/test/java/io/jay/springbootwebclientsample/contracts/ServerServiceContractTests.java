package io.jay.springbootwebclientsample.contracts;

import io.jay.springbootwebclientsample.user.User;
import io.jay.springbootwebclientsample.user.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "io.jay:server:+:stubs:0.0.1:8081")
public class ServerServiceContractTests {

    @Autowired
    private UserService userService;


    @Test
    void test_fetchUser_contract() {
        Mono<User> userMono = userService.fetchUser(1);

        StepVerifier.create(userMono)
                .assertNext(user -> {
                    assertThat(user.getId(), equalTo(1));
                    assertThat(user.getName(), equalTo("name"));
                    assertThat(user.getUsername(), equalTo("username"));
                    assertThat(user.getEmail(), equalTo("username@email.com"));
                })
                .verifyComplete();
    }

}
