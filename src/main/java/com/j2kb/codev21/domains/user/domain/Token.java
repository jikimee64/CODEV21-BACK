package com.j2kb.codev21.domains.user.domain;

import lombok.Getter;

@Getter
public enum Token {
    ACCESS_TOKEN("ACCESS_TOKEN"),
    REFRESH_TOKEN("REFRESH_TOKEN");

    String name;

    Token(String name) {
        this.name = name;
    }
}