package com.backend.domain.place.controller;

import com.backend.domain.place.dto.request.PlaceRecommendRequestDto;
import com.backend.domain.place.dto.response.PlaceRecommendResponseDto;
import com.backend.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    // 현재 위치 기반 관광지 추천 API
    @GetMapping("/recommendation")
    public ResponseEntity<PlaceRecommendResponseDto> getPlace(@RequestBody PlaceRecommendRequestDto requestDto) {

        PlaceRecommendResponseDto responseDto = placeService.getPlace(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
