package com.j2kb.codev21.global.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {

    private String code;

    private String message;

    private T data;

}