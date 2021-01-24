package com.home.oauthpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OauthPracticeApplication {

    // http://localhost:8080/oauth/authorize?client_id=testClientId&response_type=code
    public static void main(String[] args) {
        SpringApplication.run(OauthPracticeApplication.class, args);

    }
}
