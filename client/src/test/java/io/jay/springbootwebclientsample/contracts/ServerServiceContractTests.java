package io.jay.springbootwebclientsample.contracts;

import io.jay.springbootwebclientsample.user.User;
import io.jay.springbootwebclientsample.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "io.jay:server:+:stubs:0.0.1:8081")
public class ServerServiceContractTests {

    @Autowired
    private UserService userService;

    @Test
    void test_fetchUser_contract() {
        Mono<User> userMono = userService.fetchUser(999);

        StepVerifier.create(userMono)
                .assertNext(user -> {
                    assertThat(user.getId(), equalTo(999));
                    assertThat(user.getName(), equalTo("Jay"));
                    assertThat(user.getUsername(), equalTo("jay"));
                    assertThat(user.getEmail(), equalTo("jay@email.com"));
                })
                .verifyComplete();
    }
}
