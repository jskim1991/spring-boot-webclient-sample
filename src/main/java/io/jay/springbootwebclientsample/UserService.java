package io.jay.springbootwebclientsample;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    public Mono<User> fetchUser(int id) {
        return webClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(User.class).log();
    }

    public Flux<User> fetchUsers(List<Integer> userIds) {
        return Flux.fromIterable(userIds).log()
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(this::fetchUser)
                .ordered((u1, u2) -> u2.getId() - u1.getId());
    }

    public Mono<List<Post>> fetchPosts(int id) {
        ParameterizedTypeReference<List<Post>> reference = new ParameterizedTypeReference<>() {
        };

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/posts")
                        .queryParam("userId", id)
                        .build())
                .retrieve()
                .bodyToMono(reference).log();
    }

    public Mono<UserPosts> fetchUserAndPosts(int id) {
        Mono<User> user = fetchUser(id).subscribeOn(Schedulers.boundedElastic());
        Mono<List<Post>> posts = fetchPosts(id).subscribeOn(Schedulers.boundedElastic());
        return Mono.zip(user, posts, UserPosts::new);
    }
}
