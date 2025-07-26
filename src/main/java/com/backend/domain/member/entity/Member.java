package com.backend.domain.member.entity;

import com.backend.domain.place.enums.PlaceType;
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

    private int tourCnt;
    private int curturalCnt;
    private int shoppingCnt;
    private int restaurantCnt;

    public void updatePlaceId(Long id) {
        recentPlaceId = id;
    }

    public void updateUsername(String newName) {
        this.username = newName;
    }

    public void updateCnt(PlaceType placeType) {

        switch (placeType) {
            case TOURIST: {
                this.tourCnt++;
                break;
            }
            case CULTURAL: {
                this.curturalCnt++;
                break;
            }
            case SHOPPING: {
                this.shoppingCnt++;
                break;
            }
            case RESTAURANT: {
                this.restaurantCnt++;
                break;
            }
        }
    }

    @Builder
    public Member(String oauthId, String username, String description, String picUrl,
                  int tourCnt, int curturalCnt, int shoppingCnt, int restaurantCnt) {
        this.oauthId = oauthId;
        this.username = username;
        this.description = description;
        this.picUrl = picUrl;
        this.tourCnt = tourCnt;
        this.curturalCnt = curturalCnt;
        this.shoppingCnt = shoppingCnt;
        this.restaurantCnt = restaurantCnt;
    }

    public static Member createGuestOf(OAuth2User oAuth2User) {
        return Member.builder()
                .oauthId(oAuth2User.getName())
                .tourCnt(0)
                .curturalCnt(0)
                .shoppingCnt(0)
                .restaurantCnt(0)
                .build();
    }
}
