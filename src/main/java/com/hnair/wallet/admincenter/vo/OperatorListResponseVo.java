package com.hnair.wallet.admincenter.vo;

import com.hnair.wallet.admincenter.noused.Merchant;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Using IntelliJ IDEA.
 *
 * @author 李小鑫 at 2018/7/26 14:40
 */
@Data
public class OperatorListResponseVo implements Serializable {

    Integer pageNo;
    Integer pageSize;
    Integer totalCount;
    List<AdmincenterOperatorVo> operatorVoList;
    AdmincenterOperatorVo reqVo;
    List<Merchant> merchantList;

    public OperatorListResponseVo(Integer pageNo, Integer pageSize, Integer totalCount, List<AdmincenterOperatorVo> operatorVoList, AdmincenterOperatorVo reqVo, List<Merchant> merchantList) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.operatorVoList = operatorVoList;
        this.reqVo = reqVo;
        this.merchantList = merchantList;
    }
}
