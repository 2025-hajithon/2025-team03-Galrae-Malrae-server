package com.backend.domain.member.controller;

import com.backend.domain.member.dto.request.MemberNameRequestDto;
import com.backend.domain.member.dto.response.MemberInfoResponseDto;
import com.backend.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(description = "회원 정보 조회 API (username, 가장 많이 선택한 장소 타입 2개 리스트로 반환)")
    @GetMapping
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo() {

        MemberInfoResponseDto responseDto = memberService.getMemberInfo();
        return ResponseEntity.ok(responseDto);
    }

    @Operation(description = "회원 정보 수정 API (username 수정)")
    @PatchMapping
    public ResponseEntity<Void> updateMemberUsername(@RequestBody MemberNameRequestDto requestDto) {

        memberService.updateMamberUsername(requestDto);
        return ResponseEntity.ok().build();
    }
}
