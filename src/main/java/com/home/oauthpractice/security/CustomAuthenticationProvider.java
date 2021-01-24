package com.home.oauthpractice.security;

import com.home.oauthpractice.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        System.out.println("authentication.username = " + username);
        System.out.println("authentication.password = " + password);


        //checkLogin(username, password);
        //Collection<? extends GrantedAuthority> authorities = getAuthorities();


        // 테스트 유저 호출(만약 DB에 연동해서 불러온다면 대체해도 된다)
        UserInfo userInfo = new UserInfo();

        if(password.equals(userInfo.getPassword()) == false) {
            throw new BadCredentialsException(username);
        }
        return new UsernamePasswordAuthenticationToken(username, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     * 권한 추가
     */
    private Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return grantedAuthorityList;
    }


    private void checkLogin(String username, String password) {

        BCryptPasswordEncoder BCryptImpl = new BCryptPasswordEncoder();

        if(username.equals("test") ) {
            throw new BadCredentialsException("아이디 또는 패스워드가 틀립니다");
        }
        if(BCryptImpl.matches(password, "$2a$10$Ihw2gtO0/XSpdJftWFkjlePlCM6HoIhg44iwdc3vUUMn3EnJ3OgES")) {
            throw new BadCredentialsException("아이디 또는 패스워드가 다릅니다");
        }
    }
}