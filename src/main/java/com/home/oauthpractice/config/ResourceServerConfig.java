package com.home.oauthpractice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /*
        Though, note that if a server is configured both as a resource server and as an authorization server,
        then there are certain endpoint that require special handling.
        To avoid configuring over the top of those endpoints (like /token),
        it would be better to isolate your resource server endpoints to a targeted directory like so:
        Ref : https://docs.spring.io/spring-security-oauth2-boot/docs/2.2.x-SNAPSHOT/reference/html/boot-features-security-oauth2-resource-server.html
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/me").access("hasRole('USER')")
                .anyRequest().authenticated();
    }
}