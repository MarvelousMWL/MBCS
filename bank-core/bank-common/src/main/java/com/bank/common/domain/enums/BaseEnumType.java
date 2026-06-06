package com.bank.common.domain.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import java.util.Arrays;
import java.util.Optional;

public interface BaseEnumType extends IEnum<Integer> {
    
    Integer getCode();
    
    String getDescription();

    @Override
    default Integer getValue() {
        return getCode();
    }
    
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