package com.home.oauthpractice.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserInfo {

    private String username = "user";
    private String password = "pass";
    private List<String> roles = new ArrayList<>();

    public UserInfo() {
        roles.add("ROLE_USER");
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
