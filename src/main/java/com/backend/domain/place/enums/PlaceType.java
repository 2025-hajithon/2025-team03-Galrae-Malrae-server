package com.backend.domain.place.enums;

import lombok.Getter;

@Getter
public enum PlaceType {

    TOURIST("12"),
    CULTURAL("14"),
    SHOPPING("38"),
    RESTAURANT("39");

    private final String description;

    PlaceType(String description) {
        this.description = description;
    }
}
