package com.bank.common.infrastructure.persistence.handler;

import com.bank.common.domain.enums.BaseEnumType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseEnumTypeHandler<E extends Enum<E> & BaseEnumType> extends BaseTypeHandler<E> {
    
    private Class<E> type;
    
    public BaseEnumTypeHandler() {
    }
    
    public BaseEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, String.valueOf(parameter.getCode()));
    }
    
    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        if (code == null) return null;
        try {
            return BaseEnumType.valueOfCode(type, Integer.valueOf(code));
        } catch (Exception e) {
            for (E enumConst : type.getEnumConstants()) {
                if (enumConst.name().equals(code)) {
                    return enumConst;
                }
            }
            return null;
        }
    }
    
    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        if (code == null) return null;
        try {
            return BaseEnumType.valueOfCode(type, Integer.valueOf(code));
        } catch (Exception e) {
            for (E enumConst : type.getEnumConstants()) {
                if (enumConst.name().equals(code)) {
                    return enumConst;
                }
            }
            return null;
        }
    }
    
    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        if (code == null) return null;
        try {
            return BaseEnumType.valueOfCode(type, Integer.valueOf(code));
        } catch (Exception e) {
            for (E enumConst : type.getEnumConstants()) {
                if (enumConst.name().equals(code)) {
                    return enumConst;
                }
            }
            return null;
        }
    }
}