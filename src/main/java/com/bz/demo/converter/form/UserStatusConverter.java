package com.bz.demo.converter.form;

import com.bz.demo.model.common.UserStatus;
import org.springframework.core.convert.converter.Converter;

public class UserStatusConverter implements Converter<String, UserStatus> {
    @Override public UserStatus convert(String source) {
        try {
            return UserStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
