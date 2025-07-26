package com.backend.domain.place.entity;

import com.backend.domain.place.enums.PlaceType;
import com.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private String name;

    private PlaceType placeType;

    @Column(name = "image_url")
    private String imageUrl;

    private String address;

    private String mapX;

    private String mapY;

    private String description;

    @Column(name = "visit_date")
    private LocalDateTime visitDate;

    @Builder
    public Place(String name, PlaceType placeType, String imageUrl, String address, String mapX, String mapY, String description) {

        this.name = name;
        this.placeType = placeType;
        this.imageUrl = imageUrl;
        this.address = address;
        this.mapX = mapX;
        this.mapY = mapY;
        this.description = description;
    }
}
