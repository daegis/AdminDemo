package com.hnair.wallet.admincenter.vo;

import lombok.Data;

/**
 * Using IntelliJ IDEA.
 *
 * @author 李小鑫 at 2018/7/25 10:33
 */
@Data
public class AJAXResult {
    private boolean success;
    private String msg;
    private Object data;
}
