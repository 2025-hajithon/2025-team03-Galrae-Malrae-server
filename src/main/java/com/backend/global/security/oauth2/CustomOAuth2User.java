package com.backend.global.security.oauth2;

import com.backend.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final Member member;

    public CustomOAuth2User(OAuth2User oAuth2User, Member member) {
        super(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), "sub");
        this.member = member;
    }
}
