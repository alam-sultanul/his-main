package com.bz.demo.model.common;


import com.bz.demo.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@Getter
@AllArgsConstructor
public enum UserType {
    ADMIN(1,"Admin");

    private int id;
    private String name;

    public static UserType getById(int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
