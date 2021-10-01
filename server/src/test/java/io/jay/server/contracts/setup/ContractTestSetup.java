package io.jay.server.contracts.setup;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public abstract class ContractTestSetup {

    @Autowired
    ApplicationContext context;

    @BeforeEach
    void setup() {
        RestAssuredWebTestClient.applicationContextSetup(context);
    }
}
