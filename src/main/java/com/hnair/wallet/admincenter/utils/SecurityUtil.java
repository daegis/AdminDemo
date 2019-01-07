package com.hnair.wallet.admincenter.utils;

import com.hnair.wallet.admincenter.model.AdmincenterOperator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Using IntelliJ IDEA.
 * 安全校验工具类
 *
 * @author XIANYINGDA at 7/24/2018 5:01 PM
 */
public abstract class SecurityUtil {

    public static AdmincenterOperator getCurrentOpeartor() {
        SecurityContext context = SecurityContextHolder.getContext();
        AdmincenterOperator op = (AdmincenterOperator) context.getAuthentication().getPrincipal();
        return op;
    }
}
