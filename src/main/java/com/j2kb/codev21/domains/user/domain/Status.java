package com.j2kb.codev21.domains.user.domain;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE("활동 상태"),
    INACTIVE("정지 상태");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
