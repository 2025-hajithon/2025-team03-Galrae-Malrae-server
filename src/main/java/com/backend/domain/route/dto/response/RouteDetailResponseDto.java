package com.backend.domain.route.dto.response;

public record RouteDetailResponseDto(
        String image,
        String name,
        String description,
        String time
) {
}
