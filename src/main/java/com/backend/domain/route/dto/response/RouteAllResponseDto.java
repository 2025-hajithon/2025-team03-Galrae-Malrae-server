package com.backend.domain.route.dto.response;

import java.util.List;

public record RouteAllResponseDto(

        String date,
        List<RouteDetailResponseDto> routes
) {
}
