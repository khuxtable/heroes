package org.kathrynhuxtable.heroes.createdb;

import java.util.List;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "createdb")
@Data
public class InitialData {

    private List<UserData> users;
    private List<HeroData> heroes;

    @Data
    public static class UserData {
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String preferredTheme;
        private List<String> privileges;
    }

    @Data
    public static class HeroData {
        private String name;
        private String alterEgo;
        private String power;
        private Integer rating;
    }
}
