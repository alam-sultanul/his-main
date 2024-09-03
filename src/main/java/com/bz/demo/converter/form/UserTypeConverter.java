package com.bz.demo.converter.form;

import com.bz.demo.model.common.UserType;
import org.springframework.core.convert.converter.Converter;

public class UserTypeConverter implements Converter<String, UserType> {
    @Override public UserType convert(String source) {
        try {
            return UserType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
