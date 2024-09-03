package com.bz.demo.converter.attribute;


import com.bz.demo.model.common.UserStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {
    @Override public Integer convertToDatabaseColumn(UserStatus status) {
        return status.getId();
    }

    @Override public UserStatus convertToEntityAttribute(Integer id) {
        return UserStatus.getById(id);
    }
}
