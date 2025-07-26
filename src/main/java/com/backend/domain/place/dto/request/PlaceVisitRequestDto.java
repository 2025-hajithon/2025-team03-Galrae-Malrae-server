package com.backend.domain.place.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceVisitRequestDto(
        @Schema(description = "경도")
        String mapX,
        @Schema(description = "위도")
        String mapY
) {
}
