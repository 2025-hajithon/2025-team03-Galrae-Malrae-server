package com.backend.domain.member.dto.response;

import java.util.List;

public record MemberInfoResponseDto(

        String name,

        List<String> category
) {
}
