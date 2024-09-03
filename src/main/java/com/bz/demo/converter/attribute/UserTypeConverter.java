package com.bz.demo.converter.attribute;

import com.bz.demo.model.common.UserType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, Integer> {
    @Override public Integer convertToDatabaseColumn(UserType type) {
        return type.getId();
    }

    @Override public UserType convertToEntityAttribute(Integer id) {
        return UserType.getById(id);
    }
}
