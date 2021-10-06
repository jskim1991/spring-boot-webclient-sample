package io.jay.server.contracts.setup;

import io.jay.server.User;
import io.jay.server.UserRepository;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public abstract class ContractTestSetup {

    @Autowired
    ApplicationContext context;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setId(999L);
        user.setName("random user name");
        user.setUsername("random username");
        user.setEmail("randomuser@email.com");
        when(userRepository.findById(999L))
                .thenReturn(Mono.just(user));

        RestAssuredWebTestClient.applicationContextSetup(context);
    }
}
