package com.bz.demo.model.doc;

import com.bz.demo.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocumentSource {
    USER_PHOTO(1, "userPhoto", "userPhoto");

    private int id;
    private String sourceName;
    private String directoryName;

    public static DocumentSource getById(int id) {
        return Arrays.stream(values())
                .filter(documentSource -> documentSource.getId() == id)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
