package com.bz.demo.model.common;

import com.bz.demo.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE(1, "Active"),
    INACTIVE(2, "Inactive"),
    LOCKED(3, "Locked");

    private int id;
    private String name;

    public static UserStatus getById(int id) {
        return Arrays.stream(values())
                .filter(status -> status.id == id)
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
