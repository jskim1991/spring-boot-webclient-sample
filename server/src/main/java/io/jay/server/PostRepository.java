package io.jay.server;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PostRepository extends ReactiveCrudRepository<Post, Integer> {
    Flux<Post> findByUserId(Long userId);
}
