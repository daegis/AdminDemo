package com.hnair.wallet.admincenter.constant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Using IntelliJ IDEA.
 *
 * @author 李小鑫 at 2018/7/26 10:21
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RoleTypeEnum {

    ADMIN_READ(1,"管理员可读"),
    ADMIN_WRITE(2,"管理员可写"),
    MERCHANT_READ(3,"商户可读"),
    MERCHANT_WRITE(4,"商户可写");

    @Getter
    private final  Integer type;

    @Getter
    private final  String description;

    private final static Map<Integer, RoleTypeEnum> TYPE_MAP = new LinkedHashMap<>();

    static {
        for (RoleTypeEnum type : RoleTypeEnum.values()) {
            TYPE_MAP.put(type.getType(), type);
        }
    }

    public static Map<Integer, RoleTypeEnum> getRoleTypeEnums() {
        return TYPE_MAP;
    }

    public static RoleTypeEnum of(Integer type) {
        if (type == null) {
            return null;
        }
        return TYPE_MAP.get(type);
    }


    public static RoleTypeEnum getRoleTypeEnum(Integer chiperType) {
        if(chiperType==null){
            return null;
        }
        for (RoleTypeEnum type : RoleTypeEnum.values()) {
            if (type.getType().equals(chiperType)) {
                return type;
            }
        }
        return null;
    }
}
