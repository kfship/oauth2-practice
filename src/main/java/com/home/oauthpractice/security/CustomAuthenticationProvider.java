package com.home.oauthpractice.security;

import com.home.oauthpractice.entity.UserInfo;
import com.home.oauthpractice.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;


import static com.home.oauthpractice.security.AuthoritiesUtils.createAuthorities;

@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        System.out.println("authentication.username = " + username);
        System.out.println("authentication.password = " + password);

        UserInfo userInfo = null;
        if(!StringUtils.isEmpty(username)) {
            userInfo = userInfoRepository.findByUsername(username);
        }

        if(ObjectUtils.isEmpty(userInfo)) {
            throw new UsernameNotFoundException("Invalid username");
        }

        String userPassword = userInfo.getPassword();
        System.out.println("사용자 패스워드 = " + userPassword + " 입력된 패스워드 = " + password);
        if(!StringUtils.equals(password, passwordEncoder.encode(String.valueOf(password)))) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userInfo, password, createAuthorities(userInfo));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}