package io.jay.server;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public class User {
    @Id
    private Long id;
    private String name;
    private String username;
    private String email;

    public static User sample() {
        User user = new User();
        String uuid = UUID.randomUUID().toString();
        user.setName("name_" + uuid);
        user.setUsername("username_" + uuid);
        user.setEmail(uuid + "@email.com");
        return user;
    }
}
