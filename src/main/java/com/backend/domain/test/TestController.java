package com.backend.domain.test;

import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.place.entity.Place;
import com.backend.domain.place.repository.PlaceRepository;
import com.backend.domain.route.entity.Route;
import com.backend.domain.route.repository.RouteRepository;
import com.backend.domain.routePlace.entity.RoutePlace;
import com.backend.domain.routePlace.repository.RoutePlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final RouteRepository routeRepository;
    private final RoutePlaceRepository routePlaceRepository;

    @PostMapping("/create/member")
    public void createMember() {

        Member member = Member.builder()
                .username("test_user")
                .build();
        memberRepository.save(member);
    }

    @PostMapping("/create/route-place")
    public void createRoutePlace() {

        Member member = memberRepository.findById(1L)
                .orElse(null);

        Place place = placeRepository.findById(1L)
                .orElse(null);

        Route route = Route.builder()
                .name("2025.07.25")
                .member(member)
                .build();

        RoutePlace routePlace = RoutePlace.builder()
                .route(route)
                .place(place)
                .build();

        routeRepository.save(route);
        routePlaceRepository.save(routePlace);
    }
}
