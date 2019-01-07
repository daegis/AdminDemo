package com.hnair.wallet.admincenter.vo;

import lombok.Data;

import java.util.List;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/27/2018 11:35 AM
 */
@Data
public class DatagridResponse<T> {
    private Integer code = 0;
    private String msg = "";
    private Integer count;
    private List<T> data;
}
