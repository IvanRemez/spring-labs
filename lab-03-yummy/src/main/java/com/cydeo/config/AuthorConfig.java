package com.cydeo.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "author")
public class AuthorConfig {

    private String name;
    private String surname;
    private String phone;
    private List<String> socialMedia;
    private String email;
}
