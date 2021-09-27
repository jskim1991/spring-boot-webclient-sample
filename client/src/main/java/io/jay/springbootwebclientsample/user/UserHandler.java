package io.jay.springbootwebclientsample.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserService userService;

    public Mono<ServerResponse> getUser(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<User> userMono = userService.fetchUser(Integer.parseInt(id));
        return ok().body(userMono, User.class);
    }

    public Mono<ServerResponse> getUsers(ServerRequest request) {
        Optional<String> limit = request.queryParam("limit");
        int numberOfUsers = Integer.parseInt(limit.get());
        Flux<User> userFlux = userService.fetchUsers(numberOfUsers);
        return ok().body(userFlux, User.class);
    }

    public Mono<ServerResponse> getPostsByUser(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        Mono<UserPosts> userPostsMono = userService.fetchUserPosts(id);
        return ok().body(userPostsMono, UserPosts.class);
    }

    public Mono<ServerResponse> getPostsByUsers(ServerRequest request) {
        Optional<String> limit = request.queryParam("limit");
        int numberOfUsers = Integer.parseInt(limit.get());
        Flux<UserPosts> userPostsFlux = userService.fetchMultipleUserPosts(numberOfUsers);
        return ok().body(userPostsFlux, UserPosts.class);

    }
}
