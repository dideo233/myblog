package com.mycustomblog.blog.config.auth.userinfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProviderType {
    GOOGLE("google"),
    KAKAO("kakao");

    private final String value;
}
