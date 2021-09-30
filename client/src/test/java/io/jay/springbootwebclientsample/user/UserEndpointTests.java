package io.jay.springbootwebclientsample.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserEndpointTests {

    private UserService userService;
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        UserHandler userHandler = new UserHandler(userService);
        RouterFunction<?> routes = new UserEndpointConfiguration()
                .userApis(userHandler);
        client = WebTestClient
                .bindToRouterFunction(routes)
                .build();
    }

    @Test
    void test_getUsers_returnsOK_withUserList() {
        when(userService.fetchUsers(1))
                .thenReturn(Flux.just(userWithId(1)));


        client.get()
                .uri(uriBuilder -> uriBuilder.path("/users")
                        .queryParam("limit", "1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
        ;
    }

    @Test
    void test_getUser_returnsOK_withUser() {
        when(userService.fetchUser(1))
                .thenReturn(Mono.just(userWithId(1)));


        client.get()
                .uri(uriBuilder -> uriBuilder.path("/users/{id}")
                        .build(1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
        ;
    }

    @Test
    void test_getPostsByUser_returnsOk_withUserAndPostList() {
        when(userService.fetchUserPosts(1))
                .thenReturn(Mono.just(new UserPosts(userWithId(1), postsByUserId(1))));


        client.get()
                .uri(uriBuilder -> uriBuilder.path("/users/{id}/posts")
                        .queryParam("limit", "1")
                        .build(1))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.user.id").isEqualTo(1)
                .jsonPath("$.posts[0].id").isEqualTo(1)
                .jsonPath("$.posts[0].userId").isEqualTo(1)
                .jsonPath("$.posts[0].title").isNotEmpty()
                .jsonPath("$.posts[0].body").isNotEmpty()
        ;
    }

    private User userWithId(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private List<Post> postsByUserId(int userId) {
        Post post = new Post();
        post.setId(1);
        post.setUserId(userId);
        post.setTitle(UUID.randomUUID().toString());
        post.setBody(UUID.randomUUID().toString());
        return Collections.singletonList(post);
    }
}
