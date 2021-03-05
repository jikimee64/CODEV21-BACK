package com.j2kb.codev21.domains.user.domain;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE("활동"),
    INACTIVE("정지"),
    MILITARY("군복무");

    private String status;

    Status(String status) {
        this.status = status;
    }
}
