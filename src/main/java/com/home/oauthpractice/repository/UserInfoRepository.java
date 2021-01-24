package com.home.oauthpractice.repository;

import com.home.oauthpractice.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    UserInfo findByUsername(String username);
}

