package com.backend.domain.place.dto.response;

public record PlaceVisitResponseDto(
        String placeName,
        String date,
        String time,
        String image
) {
}
