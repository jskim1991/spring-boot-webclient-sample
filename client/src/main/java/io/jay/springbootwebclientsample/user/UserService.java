package io.jay.springbootwebclientsample.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder, @Value("${server-url:http://localhost:8081}") String url) {
        this.webClient = webClientBuilder
                .baseUrl(url)
                .build();
    }

    public Mono<User> fetchUser(int id) {
        return webClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(User.class);
    }

    public Flux<User> fetchUsers(int numberOfUsers) {
        return Flux.range(1, numberOfUsers).log()
                .flatMap(this::fetchUser);
    }

    public Flux<Post> fetchPosts(int id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/posts")
                        .queryParam("userId", id)
                        .build())
                .retrieve()
                .bodyToFlux(Post.class);
    }

    public Mono<UserPosts> fetchUserPosts(int id) {
        return fetchUser(id)
                .flatMap(user -> fetchPosts(user.getId()).collectList()
                        .map(posts -> new UserPosts(user, posts)));

        /* alternative way
        Mono<User> user = fetchUser(id);
        Mono<List<Post>> posts = fetchPosts(id).collectList();
        return Mono.zip(user, posts, UserPosts::new);
        */
    }

    public Flux<UserPosts> fetchMultipleUserPosts(int numberOfUsers) {
        Flux<UserPosts> userPostsFlux = Flux.range(1, numberOfUsers).log()
                .flatMap(id -> {
                    Mono<User> user = fetchUser(id);
                    Mono<List<Post>> posts = fetchPosts(id).collectList();
                    return Mono.zip(user, posts)
                            .flatMap(t -> Mono.just(new UserPosts(t.getT1(), t.getT2())));
                });
        return userPostsFlux;
    }
}
