package io.jay.springbootwebclientsample.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public Mono<User> getUser(@PathVariable int id) {
        return userService.fetchUser(id);
    }

    @GetMapping("/users")
    public Flux<User> getUsers(@RequestParam int limit) {
        return userService.fetchUsers(limit);
    }

    @GetMapping("/users/{id}/posts")
    public Mono<UserPosts> getPostsByUser(@PathVariable int id) {
        return userService.fetchUserPosts(id);
    }

    @GetMapping("/users/posts")
    public Flux<UserPosts> getPostsByUsers(@RequestParam int limit) {
        return userService.fetchMultipleUserPosts(limit);
    }
}
