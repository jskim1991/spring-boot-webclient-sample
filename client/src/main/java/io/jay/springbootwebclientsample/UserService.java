package io.jay.springbootwebclientsample;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8081")
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

    public Mono<List<Post>> fetchPosts(int id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/posts")
                        .queryParam("userId", id)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    public Mono<UserPosts> fetchUserPosts(int id) {
        Mono<User> user = fetchUser(id);
        Mono<List<Post>> posts = fetchPosts(id);
        return Mono.zip(user, posts, UserPosts::new);
    }

    public Flux<UserPosts> fetchMultipleUserPosts(int numberOfUsers) {
        Flux<UserPosts> userPostsFlux = Flux.range(1, numberOfUsers).log()
                .flatMap(id -> {
                    Mono<User> user = fetchUser(id);
                    Mono<List<Post>> posts = fetchPosts(id);
                    return Mono.zip(user, posts)
                            .flatMap(t -> Mono.just(new UserPosts(t.getT1(), t.getT2())));
                });
        return userPostsFlux;
    }
}
