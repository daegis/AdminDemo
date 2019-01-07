package com.hnair.wallet.admincenter.service;

import cn.aegisa.selext.dao.utils.PageFinder;
import com.hnair.wallet.admincenter.noused.Merchant;
import com.hnair.wallet.admincenter.vo.MerchanrUpdateQueryVo;
import com.hnair.wallet.admincenter.vo.MerchantDetailVo;

import java.util.List;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/21/2018 4:08 PM
 */
public interface IMerchantService {
    PageFinder<Merchant> getMerchantListOnPage(int pageSize, int pageNumber);

    MerchantDetailVo getMerchantDetail(Integer id);

    void doUpdateMerchantInfo(MerchanrUpdateQueryVo request) throws Exception;

    List<Merchant> getAllMerchantList();
}
