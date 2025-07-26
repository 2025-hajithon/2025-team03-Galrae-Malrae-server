package com.backend.domain.place.entity;

import com.backend.domain.place.enums.PlaceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private PlaceType placeType;

    @Column(name = "image_url")
    private String imageUrl;

    private String address;

    @Column(name = "visit_date")
    private LocalDateTime visitDate;
}
