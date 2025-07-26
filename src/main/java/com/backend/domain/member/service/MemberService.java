package com.backend.domain.member.service;

import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member fetchOrCreateFromOAuth(OAuth2User oAuth2User) {
        return memberRepository
                .findByOauthId(oAuth2User.getName())
                .orElseGet(() -> memberRepository.save(Member.createGuestOf(oAuth2User)));
    }
}
