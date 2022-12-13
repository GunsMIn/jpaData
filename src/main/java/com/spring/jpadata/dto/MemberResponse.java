package com.spring.jpadata.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberResponse {
    private String username;
    private int age;

    public MemberResponse() {
    }

    @QueryProjection
    public MemberResponse(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
