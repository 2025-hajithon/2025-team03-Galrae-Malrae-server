package com.backend.domain.place.dto.response;

public record PlaceRecommendResponseDto(
        String placeName,
        String address,
        String image,
        String description
) {
}
