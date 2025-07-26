package com.backend.domain.member.service;

import com.backend.domain.member.dto.request.MemberNameRequestDto;
import com.backend.domain.member.dto.response.MemberInfoResponseDto;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.global.security.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberUtil memberUtil;

    @Transactional
    public Member fetchOrCreateFromOAuth(OAuth2User oAuth2User) {
        return memberRepository
                .findByOauthId(oAuth2User.getName())
                .orElseGet(() -> memberRepository.save(Member.createGuestOf(oAuth2User)));
    }

    @Transactional
    public void updateMamberUsername(MemberNameRequestDto requestDto) {

        Long memberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원 없음"));

        member.updateUsername(requestDto.name());
        memberRepository.save(member);
    }

    private List<String> getTop2Preferences(Member member) {

        Map<String, Integer> categoryCountMap = new HashMap<>();

        categoryCountMap.put("관광지", member.getTourCnt());
        categoryCountMap.put("문화시설", member.getCurturalCnt());
        categoryCountMap.put("쇼핑", member.getShoppingCnt());
        categoryCountMap.put("음식점", member.getRestaurantCnt());

        return categoryCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public MemberInfoResponseDto getMemberInfo() {

        Long memberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원 없음"));

        return new MemberInfoResponseDto(
                member.getUsername(),
                getTop2Preferences(member)
        );
    }
}
