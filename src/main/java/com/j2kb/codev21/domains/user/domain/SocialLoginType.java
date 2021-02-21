package com.j2kb.codev21.domains.user.domain;

import lombok.Getter;

@Getter
public enum SocialLoginType {
    GITHUB("GITHUB");

    String name;

    SocialLoginType(String name) {
        this.name = name;
    }
}
