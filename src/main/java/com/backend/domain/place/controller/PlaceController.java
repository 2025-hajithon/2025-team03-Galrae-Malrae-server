package com.backend.domain.place.controller;

import com.backend.domain.place.dto.request.PlaceRecommendRequestDto;
import com.backend.domain.place.dto.request.PlaceVisitRequestDto;
import com.backend.domain.place.dto.response.PlaceRecommendResponseDto;
import com.backend.domain.place.dto.response.PlaceVisitResponseDto;
import com.backend.domain.place.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @Operation(description = "현재 위치 기반 관광지 추천 API")
    @GetMapping("/recommendation")
    public ResponseEntity<PlaceRecommendResponseDto> getPlace(@RequestBody PlaceRecommendRequestDto requestDto) {

        PlaceRecommendResponseDto responseDto = placeService.getPlace(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(description = "방문 완료 요청 API, 아직 거리 부족하면 에러 나옴")
    @PostMapping("/visit")
    public ResponseEntity<PlaceVisitResponseDto> visitPlace(@RequestBody PlaceVisitRequestDto requestDto) {

        PlaceVisitResponseDto responseDto = placeService.visitPlace(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
