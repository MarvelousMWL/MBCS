package com.bank.common.domain.enums;

import java.util.Arrays;
import java.util.Optional;

public interface BaseEnumType {
    
    Integer getCode();
    
    String getDescription();
    
    static <E extends Enum<E> & BaseEnumType> E valueOfCode(Class<E> enumType, Integer code) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code + " for enum " + enumType.getName()));
    }
    
    static <E extends Enum<E> & BaseEnumType> Optional<E> fromCode(Class<E> enumType, Integer code) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst();
    }
}