package com.hnair.wallet.admincenter.vo;

import com.hnair.wallet.admincenter.model.AdmincenterOperator;
import lombok.Data;

import java.io.Serializable;

/**
 * Using IntelliJ IDEA.
 *
 * @author 李小鑫 at 2018/7/23 15:18
 */
@Data
public class AdmincenterOperatorVo implements Serializable {

    private Integer id;

    private String operatorName;

    private String operatorUniqueSalt;

    private String password;

    private String operatorNickName;

    private String operatorSource;

    private String authorityName;

    private String merchantName;
}
