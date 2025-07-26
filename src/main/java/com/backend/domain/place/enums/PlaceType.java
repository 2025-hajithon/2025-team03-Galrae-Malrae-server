package com.backend.domain.place.enums;

import lombok.Getter;

@Getter
public enum PlaceType {

    TOURIST("12", "관광지"),
    CULTURAL("14", "문화시설"),
    SHOPPING("38", "쇼핑"),
    RESTAURANT("39", "음식점");

    private final String description;
    private final String koreanType;

    PlaceType(String description, String koreanType) {
        this.description = description;
        this.koreanType = koreanType;
    }
}
