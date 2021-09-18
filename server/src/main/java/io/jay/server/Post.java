package io.jay.server;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Post {
    @Id
    private int id;
    private long userId;
    private String title;
    private String body;

    public static List<Post> samples(long userId) {
        Random random = new Random();
        int max = random.nextInt(9) + 1;

        List<Post> collect = IntStream.range(1, max)
                .mapToObj(i -> {
                    Post post = new Post();
                    String uuid = UUID.randomUUID().toString();
                    post.setUserId(userId);
                    post.setBody("body_" + uuid);
                    post.setTitle("title_" + uuid);
                    return post;
                })
                .collect(Collectors.toList());
        return collect;
    }
}