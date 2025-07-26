package com.backend.domain.route.repository;

import com.backend.domain.member.entity.Member;
import com.backend.domain.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByNameAndMember(String name, Member member);

    List<Route> findByMemberOrderByCreatedAtDesc(Member member);
}
