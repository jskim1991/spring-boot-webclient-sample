package io.jay.springbootwebclientsample;

import lombok.Data;

import java.util.List;

@Data
public class UserPosts {
    private User user;
    private List<Post> posts;

    public UserPosts(User user, List<Post> posts) {
        this.user = user;
        this.posts = posts;
    }
}
