package com.hnair.wallet.admincenter.model;

import lombok.Data;

import java.io.Serializable;


/**
 * AdmincenterRoleAuthority Entity.
 */
@Data
public class AdmincenterRoleAuthority implements Serializable {

    //列信息
    private Integer id;

    private Integer roleId;

    private Integer authorityId;


}

