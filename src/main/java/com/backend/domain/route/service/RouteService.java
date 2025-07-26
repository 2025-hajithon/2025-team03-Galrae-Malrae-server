package com.backend.domain.route.service;

import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.place.dto.response.PlaceVisitResponseDto;
import com.backend.domain.route.dto.response.RouteAllResponseDto;
import com.backend.domain.route.dto.response.RoutePreviewResponse;
import com.backend.domain.route.entity.Route;
import com.backend.domain.route.repository.RouteRepository;
import com.backend.domain.routePlace.entity.RoutePlace;
import com.backend.domain.routePlace.repository.RoutePlaceRepository;
import com.backend.global.security.util.MemberUtil;
import com.backend.global.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RouteService {

    private final RouteRepository routeRepository;
    private final MemberRepository memberRepository;
    private final RoutePlaceRepository routePlaceRepository;

    private final FormatUtil formatUtil;
    private final MemberUtil memberUtil;

    public List<PlaceVisitResponseDto> getAllCard() {

        Long memberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원 없음"));

        List<Route> routes = routeRepository.findByMemberOrderByCreatedAtDesc(member);
        List<PlaceVisitResponseDto> responseDtos = new ArrayList<>();

        for (Route route : routes) {

            List<RoutePlace> routePlaces = routePlaceRepository.findByRouteOrderByCreatedAtAsc(route);

            for(RoutePlace routePlace : routePlaces) {

                String formattedDate = formatUtil.formatDate(routePlace.getCreatedAt());
                String formattedTime = formatUtil.formatTime(routePlace.getCreatedAt());

                PlaceVisitResponseDto responseDto = new PlaceVisitResponseDto(
                        routePlace.getPlace().getName(),
                        formattedDate,
                        formattedTime,
                        routePlace.getPlace().getImageUrl()
                );
                responseDtos.add(responseDto);
            }
        }
        return responseDtos;
    }

    public List<RouteAllResponseDto> getAllRoute() {

        Long memberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원 없음"));

        List<Route> routes = routeRepository.findByMemberOrderByCreatedAtDesc(member);
        List<RouteAllResponseDto> responseDtos = new ArrayList<>();

        for (Route route : routes) {

            List<RoutePlace> routePlaces = routePlaceRepository.findByRouteOrderByCreatedAtAsc(route);
            List<RoutePreviewResponse> responses = new ArrayList<>();

            for(RoutePlace routePlace : routePlaces) {

                RoutePreviewResponse routePreviewResponse = new RoutePreviewResponse(
                        routePlace.getPlace().getName(),
                        routePlace.getPlace().getImageUrl()
                );
                responses.add(routePreviewResponse);
            }

            RouteAllResponseDto responseDto = new RouteAllResponseDto(
                    route.getName(),
                    responses
            );
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

    public List<RoutePreviewResponse> getTodayRoute() {

        Long memberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원 없음"));

        // 오늘 Route 가져오기
        String formattedDate = formatUtil.formatDate(LocalDateTime.now());
        Route route = routeRepository.findByNameAndMember(formattedDate, member)
                .orElse(null);

        List<RoutePreviewResponse> responses = new ArrayList<>();
        if (route != null) {

            List<RoutePlace> routePlaces = routePlaceRepository.findByRouteOrderByCreatedAtAsc(route);
            for (RoutePlace routePlace : routePlaces) {

                RoutePreviewResponse response = new RoutePreviewResponse(
                        routePlace.getPlace().getName(),
                        routePlace.getPlace().getImageUrl()
                );
                responses.add(response);
            }
        }
        return responses;
    }
}
