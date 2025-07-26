package com.backend.domain.place.service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.domain.place.dto.request.PlaceRecommendRequestDto;
import com.backend.domain.place.dto.request.PlaceVisitRequestDto;
import com.backend.domain.place.dto.response.PlaceRecommendResponseDto;
import com.backend.domain.place.dto.response.PlaceVisitResponseDto;
import com.backend.domain.place.entity.Place;
import com.backend.domain.place.repository.PlaceRepository;
import com.backend.domain.route.entity.Route;
import com.backend.domain.route.repository.RouteRepository;
import com.backend.domain.routePlace.entity.RoutePlace;
import com.backend.domain.routePlace.repository.RoutePlaceRepository;
import com.backend.global.security.util.MemberUtil;
import com.backend.global.util.FormatUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    @Value("${tour.api}")
    private String tourApiKey;

    private final PlaceRepository placeRepository;
    private final MemberRepository memberRepository;
    private final RouteRepository routeRepository;
    private final RoutePlaceRepository routePlaceRepository;

    private final MemberUtil memberUtil;
    private final FormatUtil formatUtil;

    @Transactional
    public PlaceVisitResponseDto visitPlace(PlaceVisitRequestDto requestDto) {
        Long memberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원 없음"));

        Place place = placeRepository.findById(member.getRecentPlaceId())
                .orElseThrow(() -> new RuntimeException("해당 장소 없음"));

        if (!isWithinRange(place.getMapX(), place.getMapY(), requestDto.mapX(), requestDto.mapY())) {
            throw new IllegalArgumentException("아직 도착하지 않았습니다.");
        }

        Route route = getOrCreateTodayRoute(member);
        saveRoutePlaceMapping(route, place);

        LocalDateTime now = LocalDateTime.now();
        String formattedDate = formatUtil.formatDate(now);
        String formattedTime = formatUtil.formatTime(now);

        return new PlaceVisitResponseDto(place.getName(), formattedDate, formattedTime, place.getImageUrl());
    }

    private Route getOrCreateTodayRoute(Member member) {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        return routeRepository.findByNameAndMember(today, member)
                .orElseGet(() -> {
                    Route newRoute = Route.builder()
                            .name(today)
                            .member(member)
                            .build();
                    return routeRepository.save(newRoute);
                });
    }

    private void saveRoutePlaceMapping(Route route, Place place) {
        RoutePlace routePlace = RoutePlace.builder()
                .route(route)
                .place(place)
                .build();
        routePlaceRepository.save(routePlace);
    }

    private boolean isWithinRange(String targetX, String targetY, String currentX, String currentY) {
        try {
            double targetXd = Double.parseDouble(targetX);
            double targetYd = Double.parseDouble(targetY);
            double currentXd = Double.parseDouble(currentX);
            double currentYd = Double.parseDouble(currentY);

            double tolerance = 0.0005;

            return Math.abs(targetXd - currentXd) <= tolerance &&
                    Math.abs(targetYd - currentYd) <= tolerance;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Transactional
    public PlaceRecommendResponseDto getPlace(PlaceRecommendRequestDto requestDto) {

        Long memberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원 없음"));

        try {
            JsonNode itemsNode = fetchItemsFromApi(requestDto);
            if (itemsNode == null || !itemsNode.isArray() || itemsNode.isEmpty()) {
                return null;
            }

            // 랜덤 추천
            JsonNode selectedItem = getRandomItem(itemsNode);
            String placeName = selectedItem.path("title").asText();
            String address = selectedItem.path("addr1").asText();
            String imageUrl = selectedItem.path("firstimage").asText();
            String mapX = selectedItem.path("mapx").asText();
            String mapY = selectedItem.path("mapy").asText();

            // Place 저장 또는 조회
            Place place = placeRepository.findByName(placeName)
                    .orElseGet(() -> placeRepository.save(
                            Place.builder()
                                    .name(placeName)
                                    .placeType(requestDto.placeType())
                                    .imageUrl(imageUrl)
                                    .address(address)
                                    .mapX(mapX)
                                    .mapY(mapY)
                                    .build()
                    ));

            // 가장 최근 추천지 member에 저장
            member.updatePlaceId(place.getId());

            return new PlaceRecommendResponseDto(
                    placeName,
                    address,
                    imageUrl,
                    "" // [TODO] description 처리 필요 시 수정
            );

        } catch (Exception e) {
            System.err.println("[PlaceService] API 요청 실패: " + e.getMessage());
            return null;
        }
    }

    private JsonNode fetchItemsFromApi(PlaceRecommendRequestDto requestDto) throws Exception {
        String url = String.format(
                "https://apis.data.go.kr/B551011/KorService2/locationBasedList2"
                        + "?MobileOS=ETC"
                        + "&MobileApp=GalraeMalrae"
                        + "&mapX=%s"
                        + "&mapY=%s"
                        + "&radius=%s"
                        + "&serviceKey=%s"
                        + "&contentTypeId=%s"
                        + "&_type=json",
                requestDto.mapX(),
                requestDto.mapY(),
                requestDto.radius(),
                tourApiKey,
                requestDto.placeType().getDescription()
        );

        // request
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        // response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());

        return rootNode.path("response").path("body").path("items").path("item");
    }

    private JsonNode getRandomItem(JsonNode items) {
        int index = new Random().nextInt(items.size());
        return items.get(index);
    }
}
