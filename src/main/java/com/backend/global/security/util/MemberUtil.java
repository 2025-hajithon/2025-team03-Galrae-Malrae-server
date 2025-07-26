package com.backend.global.security.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MemberUtil {

    public Long getCurrentMemberId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
