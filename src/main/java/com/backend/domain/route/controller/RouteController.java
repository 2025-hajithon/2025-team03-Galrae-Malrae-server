package com.backend.domain.route.controller;

import com.backend.domain.place.dto.response.PlaceVisitResponseDto;
import com.backend.domain.route.dto.response.RouteAllResponseDto;
import com.backend.domain.route.dto.response.RouteDetailResponseDto;
import com.backend.domain.route.dto.response.RoutePreviewResponse;
import com.backend.domain.route.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
public class RouteController {

    private final RouteService routeService;

    @Operation(description = "[메인 페이지] 오늘 루트 조회하는 API")
    @GetMapping("/today")
    public ResponseEntity<List<RoutePreviewResponse>> getTodayRoute() {

        List<RoutePreviewResponse> responses = routeService.getTodayRoute();
        return ResponseEntity.ok(responses);
    }

    @Operation(description = "[마이 페이지] 전체 방문 루트 조회하는 API")
    @GetMapping("/all")
    public ResponseEntity<List<RouteAllResponseDto>> getAllRoute() {

        List<RouteAllResponseDto> responseDtos = routeService.getAllRoute();
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(description = "[마이 페이지] 전체 추천지 카드 조회하는 API")
    @GetMapping("/card")
    public ResponseEntity<List<PlaceVisitResponseDto>> getAllCard() {

        List<PlaceVisitResponseDto> responseDtos = routeService.getAllCard();
        return ResponseEntity.ok(responseDtos);
    }
}
