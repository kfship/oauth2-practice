package com.home.oauthpractice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    //-----------------------------------------------------------------------------------------------------------------------------------------------
    // 기본적으로 Authorization Code Grant 타입에서는 이 부분을 주석처리 해도 잘 작동한다.
    //   -- AuthenticatinoManager를 사용하지 않더라도, Spring Security의 로그인만 처리할 수 있다면 실제로 Access Token 발급할 때 End User의 정보는 필요없다
    //   -- CustomAuthenticationProvider를 구현한 후 어디에도 명시적으로 주입하지 않았지만 스프링 시큐리티가 알아서 불러온 후 사용함

    // 하지만 Resource Owner Password Credential Grant 타입에서는 이 부분이 필요하다
    //   -- 토큰을 발급할 때 End User의 정보를 불러와서 인증 후에 토큰을 발급하기 때문이다

    private AuthenticationManager authenticationManager;
    private DataSource dataSource;

    public AuthServerConfig(AuthenticationConfiguration authenticationConfiguration, DataSource dataSource) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.dataSource = dataSource;
    }

    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);  // Password Grant 타입에서 사용자 인증을 어떻게 할지 결정
        endpoints.tokenStore(jdbcTokenStore());                  // 발급된 Access Token 관리를 어떻게 할지 결정
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                .withClient("testClientId")
                .secret("{noop}testSecret")
                .redirectUris("http://localhost:8080/oauth2/callback")
                .authorizedGrantTypes("authorization_code", "password", "refresh_token")
                .scopes("read", "write")
        ;
    }

//    **************************************************************************************************************
//    스프링 시큐리티의 설정과 중복되기 때문에 여기서는 아무것도 할 필요가 없는게 아닌가...
//    **************************************************************************************************************
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 토큰유효성(/token/check_token) 접근을 위해 설정
        security.checkTokenAccess("permitAll()");
    }
}