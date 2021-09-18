package io.jay.server;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DatabaseConfiguration {

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

        return initializer;
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository, PostRepository postRepository) {

        return (args) -> {
            Flux.range(1, 100)
                    .concatMap(id -> userRepository.saveAll(Arrays.asList(User.sample())))
                    .blockLast(Duration.ofSeconds(10));

            Flux.range(1, 100)
                    .concatMap(id -> postRepository.saveAll(Post.samples(id)))
                    .blockLast(Duration.ofSeconds(10));
        };
    }
}
