package io.jay.springbootwebclientsample;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public Mono<UserPosts> getPostsBySpecificUser(@PathVariable int id) {
        return userService.fetchUserPosts(id);
    }

    @GetMapping("/users/posts")
    public Flux<UserPosts> getPostsByUsers(@RequestParam int limit) {
        return userService.fetchMultipleUserPosts(limit);
    }
}
