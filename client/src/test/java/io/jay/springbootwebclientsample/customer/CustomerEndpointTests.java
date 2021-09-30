package io.jay.springbootwebclientsample.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerEndpointTests {

    private CustomerService mockCustomerService;
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        mockCustomerService = mock(CustomerService.class);
        RouterFunction<?> routes = new CustomerEndpointConfiguration()
                .customerApis(mockCustomerService);
        client = WebTestClient.bindToRouterFunction(routes)
                .build();
    }

    @Test
    void test_getCustomersStream_returnsSSE() {
        when(mockCustomerService.getCustomersStream())
                .thenReturn(Flux.fromIterable(
                        Arrays.asList(new Customer(1, "customer1"), new Customer(2, "customer2"))));


        Flux<Customer> take = client.get()
                .uri("/customers/stream")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
                .returnResult(Customer.class)
                .getResponseBody();


        StepVerifier.create(take)
                .expectNextCount(2)
                .verifyComplete();
    }
}
