package com.hnair.wallet.admincenter.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;


/**
 * AdmincenterAuthority Entity.
 */
@Data
public class AdmincenterAuthority implements GrantedAuthority, Serializable {

    //列信息
    private Integer id;

    private String authorityName;

    private Integer pid;

    private String icon;

    private String description;

    @Override
    public String getAuthority() {
        return authorityName;
    }

}

