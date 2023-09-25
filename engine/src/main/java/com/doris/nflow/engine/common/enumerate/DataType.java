package com.doris.nflow.engine.common.enumerate;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: DataType
 * @Description:
 * @date: 2022/10/3 11:03
 */
public enum DataType {
    /**
     * 数据类型
     */
    STRING("string", "字符串"),
    INTEGER("integer", "数字"),
    DOUBLE("double", "小数"),
;
    private String code;


    private String desc;


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    DataType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DataType getType(String code) {
        Optional<DataType> any = Arrays.stream(values()).filter(dataType -> dataType.getCode().equals(code)).findAny();
        return any.orElse(null);
    }

    public static Object parseData(String code, Object data) {
        DataType type = getType(code);
        if (type == null) {
            return data;
        }

        Optional<Object> dataOptional = Optional.of(data);

        switch (type) {
            case STRING:
                return
                        dataOptional
                                .filter(String.class::isInstance)
                                .map(String.class::cast);
            case INTEGER:
                return
                        dataOptional
                                .filter(Integer.class::isInstance)
                                .map(Integer.class::cast);
            case DOUBLE:
                return
                        dataOptional
                                .filter(Double.class::isInstance)
                                .map(Double.class::cast);
            default:
                return data;
        }

    }
}
