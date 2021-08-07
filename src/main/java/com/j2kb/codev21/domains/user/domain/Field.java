package com.j2kb.codev21.domains.user.domain;

import lombok.Getter;

@Getter
public enum Field {
    APP("앱"),
    FRONT_END("프론트 엔드"),
    BACK_END("백 엔드"),
    DATA_SCIENCE("데이터 사이언스"),
    NONE("미소속");

    private String field;

    Field(String field) {
        this.field = field;
    }
}
