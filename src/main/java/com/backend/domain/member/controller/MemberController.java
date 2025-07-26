package com.backend.domain.member.controller;

import com.backend.domain.member.dto.request.MemberNameRequestDto;
import com.backend.domain.member.dto.response.MemberInfoResponseDto;
import com.backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/create")
    public void createMember() {
        memberService.createMember();
    }

    @GetMapping
    public ResponseEntity<MemberInfoResponseDto> getMemberInfo() {

        MemberInfoResponseDto responseDto = memberService.getMemberInfo();
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping
    public ResponseEntity<Void> updateMemberUsername(@RequestBody MemberNameRequestDto requestDto) {

        memberService.updateMamberUsername(requestDto);
        return ResponseEntity.ok().build();
    }
}
