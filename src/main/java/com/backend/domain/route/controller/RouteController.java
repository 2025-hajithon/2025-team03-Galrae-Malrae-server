package com.backend.domain.route.controller;

import com.backend.domain.route.dto.response.TodayRouteResponse;
import com.backend.domain.route.service.RouteService;
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

    @GetMapping("/today")
    public ResponseEntity<List<TodayRouteResponse>> getTodayRoute() {

        List<TodayRouteResponse> responses = routeService.getTodayRoute();
        return ResponseEntity.ok(responses);
    }
}
