package com.backend.domain.routePlace.repository;

import com.backend.domain.route.entity.Route;
import com.backend.domain.routePlace.entity.RoutePlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutePlaceRepository extends JpaRepository<RoutePlace, Long> {

    List<RoutePlace> findByRouteOrderByCreatedAtAsc(Route route);
}
