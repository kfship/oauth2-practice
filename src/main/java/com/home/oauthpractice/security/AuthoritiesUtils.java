package com.home.oauthpractice.security;

import com.home.oauthpractice.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.List;


public final class AuthoritiesUtils {

    private static final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN","ROLE_USER");
    private static final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");

    public static Collection<? extends GrantedAuthority> createAuthorities(UserInfo userInfo) {
        String userrole = userInfo.getUserrole();
        if (userrole.equals("ADMIN")) {
            return ADMIN_ROLES;
        }
        return USER_ROLES;
    }
}
