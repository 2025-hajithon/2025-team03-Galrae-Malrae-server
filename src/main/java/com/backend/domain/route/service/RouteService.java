package com.backend.domain.route.service;

import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.route.dto.response.TodayRouteResponse;
import com.backend.domain.route.entity.Route;
import com.backend.domain.route.repository.RouteRepository;
import com.backend.domain.routePlace.entity.RoutePlace;
import com.backend.domain.routePlace.repository.RoutePlaceRepository;
import com.backend.global.security.util.MemberUtil;
import com.backend.global.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final MemberRepository memberRepository;
    private final RoutePlaceRepository routePlaceRepository;

    private final FormatUtil formatUtil;
    private final MemberUtil memberUtil;

    public List<TodayRouteResponse> getTodayRoute() {

        Long memberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원 없음"));

        // 오늘 Route 가져오기
        String formattedDate = formatUtil.formatDate(LocalDateTime.now());
        Route route = routeRepository.findByNameAndMember(formattedDate, member)
                .orElse(null);

        List<TodayRouteResponse> responses = new ArrayList<>();
        if (route != null) {

            List<RoutePlace> routePlaces = routePlaceRepository.findByRouteOrderByCreatedAtAsc(route);
            for (RoutePlace routePlace : routePlaces) {

                TodayRouteResponse response = new TodayRouteResponse(
                        routePlace.getPlace().getName(),
                        routePlace.getPlace().getImageUrl()
                );
                responses.add(response);
            }
        }
        return responses;
    }
}
