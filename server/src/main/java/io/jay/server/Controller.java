package io.jay.server;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @GetMapping("/users/{id}")
    public Mono<User> findUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .delayElement(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping("/posts")
    public Flux<Post> findPostsByUserId(@RequestParam long userId) {
        return postRepository.findByUserId(userId);
    }
}
