package com.hnair.wallet.admincenter.executor;

import com.hnair.wallet.admincenter.noused.Merchant;
import com.hnair.wallet.admincenter.service.IMerchantService;
import com.hnair.wallet.admincenter.service.IOperatorService;
import com.hnair.wallet.admincenter.vo.AdmincenterOperatorVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Using IntelliJ IDEA.
 *
 * @author 李小鑫 at 2018/7/26 14:03
 */
@Slf4j
public class OperatorListExecutor implements Callable<Object> {

    IOperatorService operatorService;

    IMerchantService merchantService;

    Integer pageNo;

    Integer pageSize;

    Map<String, Object> queryMap;

    Integer type;

    public OperatorListExecutor(IOperatorService operatorService, Map<String, Object> queryMap, Integer pageNo, Integer pageSize, Integer type) {
        this.operatorService = operatorService;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.queryMap = queryMap;
        this.type = type;
    }

    public OperatorListExecutor(IOperatorService operatorService, Map<String, Object> queryMap, Integer type) {
        this.operatorService = operatorService;
        this.queryMap = queryMap;
        this.type = type;
    }

    public OperatorListExecutor(IMerchantService merchantService, Integer type) {
        this.merchantService = merchantService;
        this.type = type;
    }

    @Override
    public Object call() throws Exception {
        if (type == 1) {
            List<AdmincenterOperatorVo> authorityOperatorList = operatorService.getAuthorityOperatorList(queryMap, pageNo, pageSize);
            return authorityOperatorList;
        } else if (type == 2) {
            Integer allOperatorCount = operatorService.getAllOperatorCount(queryMap);
            return allOperatorCount;
        } else if (type == 3) {
            List<Merchant> allMerchantList = merchantService.getAllMerchantList();
            return allMerchantList;
        } else {
            return null;
        }
    }
}
