package com.backend.domain.member.entity;

import com.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "oauth_id")
    private String oauthId;

    private String username;

    private String description;

    @Column(name = "pic_url")
    private String picUrl;

    private Long recentPlaceId;

    public void updatePlaceId(Long id) {
        recentPlaceId = id;
    }

    @Builder
    public Member(String oauthId, String username, String description, String picUrl) {
        this.oauthId = oauthId;
        this.username = username;
        this.description = description;
        this.picUrl = picUrl;
    }

    public static Member createGuestOf(OAuth2User oAuth2User) {
        return Member.builder()
                .oauthId(oAuth2User.getName())
                .build();
    }
}
