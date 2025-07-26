package com.backend.domain.routePlace.repository;

import com.backend.domain.routePlace.entity.RoutePlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutePlaceRepository extends JpaRepository<RoutePlace, Long> {
}
