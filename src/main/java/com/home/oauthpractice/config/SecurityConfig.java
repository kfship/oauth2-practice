package com.home.oauthpractice.config;

import com.home.oauthpractice.security.NoEncodingPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder() {
        return new NoEncodingPasswordEncoder();
    }

    /*
     * HttpSecurity객체는 현재 로그인한 사용자가 적절한 역할과 연결돼 있는지 확인하는 서블릿 필터를 생성한다.
     * ant 패턴식 설명
     * 1) ? -> 단일 문자와 일치한다.
     * 2) * ->/를 제외하는 0자 이상의 문자와 일치한다. (ex "/events*" == "/events","/events123")
     * 3) ** ->경로의 0개 이상의 디렉터리와 일치한다. (ex "/events/**" == "/events","/events/","/events/1","/events/1/form?test=1")
     */
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
                .formLogin()
            .and()
                .httpBasic();
    }

    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //UserDetailsService&DaoAuthenticationProvider를 이용하는 인증
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        //사용자 정의 AuthenticationProvider를 이용하는 인증
        //auth.authenticationProvider(authenticationProvider);


        /*
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("{noop}pass")
                .roles("USER");
        */
    //}

    /*
     * Security Filter 적용을 무시한다.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**")
                .antMatchers("/css/**")
        ;
    }

}