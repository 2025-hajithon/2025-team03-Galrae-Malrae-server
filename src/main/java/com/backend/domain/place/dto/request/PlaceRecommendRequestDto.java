package com.backend.domain.place.dto.request;

import com.backend.domain.place.enums.PlaceType;
import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceRecommendRequestDto(

        @Schema(description = "경도")
        String mapX,
        @Schema(description = "위도")
        String mapY,
        @Schema(description = "장소 유형 : TOURIST(관광지), CULTURAL(문화시설), SHOPPING(쇼핑), RESTAURANT(레스토랑)")
        PlaceType placeType,
        @Schema(description = "반경 ")
        String radius

) {
}
