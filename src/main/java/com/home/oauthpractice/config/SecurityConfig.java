package com.home.oauthpractice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                    .antMatchers("/oauth/authorize").permitAll()
                    .antMatchers("/oauth/**", "/oauth2/callback").permitAll()
                    //.antMatchers("/api/**").access("hasRole('USER')")   //Resource Server 설정이 아니라 여기서 설정하면, 토큰과 관계없이 로그인만 거치면 자원에 접근 가능
                    //.anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password("{noop}pass")
//                .roles("USER");
//    }


}