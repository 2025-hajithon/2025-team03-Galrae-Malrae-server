package com.backend.domain.place.service;

import java.net.URI;
import java.util.Random;

import com.backend.domain.member.entity.Member;
import com.backend.domain.place.dto.request.PlaceRecommendRequestDto;
import com.backend.domain.place.dto.response.PlaceRecommendResponseDto;
import com.backend.domain.place.entity.Place;
import com.backend.domain.place.repository.PlaceRepository;
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

    @Transactional
    public PlaceRecommendResponseDto getPlace(PlaceRecommendRequestDto requestDto, Member member) {
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

            // Place 저장 또는 조회
            Place place = placeRepository.findByName(placeName)
                    .orElseGet(() -> placeRepository.save(
                            Place.builder()
                                    .name(placeName)
                                    .placeType(requestDto.placeType())
                                    .imageUrl(imageUrl)
                                    .address(address)
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
