package com.goddess.framwork.infrastructure.persistence;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

public class FieldUtils {
    public static String toDBField(String field) {
        if (StringUtils.isNoneBlank(field)) {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field);
        }
        return "";
    }
}
