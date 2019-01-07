package com.hnair.wallet.admincenter.web;

import cn.aegisa.selext.dao.utils.PageFinder;
import com.alibaba.fastjson.JSON;
import com.hnair.wallet.admincenter.model.AdmincenterOperator;
import com.hnair.wallet.admincenter.noused.Merchant;
import com.hnair.wallet.admincenter.service.IMerchantService;
import com.hnair.wallet.admincenter.vo.MerchanrUpdateQueryVo;
import com.hnair.wallet.admincenter.vo.MerchantDetailVo;
import com.hnair.wallet.admincenter.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/21/2018 4:00 PM
 */
@Controller
@Slf4j
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private IMerchantService merchantService;

    @RequestMapping("/list")
//    @PreAuthorize("hasAuthority('add')")
    public String merchantList(Model model, Integer pageNumber, Integer pageSize) {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageFinder<Merchant> pageFinder = merchantService.getMerchantListOnPage(pageSize, pageNumber);
        List<Merchant> merchantList = pageFinder.getData();
        int rowCount = pageFinder.getRowCount();
        model.addAttribute("merchantList", merchantList);
        model.addAttribute("total", rowCount);
        model.addAttribute("limit", pageSize);
        model.addAttribute("curr", pageNumber);
        return "wallet/merchant/list";
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")
    public String merchantAdd() {
        SecurityContext context = SecurityContextHolder.getContext();
        AdmincenterOperator userObj = (AdmincenterOperator) context.getAuthentication().getPrincipal();
        log.info("当前操作用户来源：{}", userObj.getOperatorSource());

        return "wallet/merchant/add";
    }

    @RequestMapping("/detail/{id}")
    public String merchantDetail(Model model, @PathVariable Integer id) {
        MerchantDetailVo merchantDetailVo = merchantService.getMerchantDetail(id);
        log.info(JSON.toJSONString(merchantDetailVo));
        model.addAttribute("merchantId", id);
        model.addAttribute("detail", merchantDetailVo);
        return "wallet/merchant/detail";
    }

    //    @PreAuthorize("hasAnyAuthority('merchant-write','admin-write')")
    @RequestMapping("/update")
    @ResponseBody
    public RestResponse merchantUpdate(MerchanrUpdateQueryVo request) {
        RestResponse response = new RestResponse();
        try {
            merchantService.doUpdateMerchantInfo(request);
            response.setSuccess(true);
        } catch (Exception e) {
            log.error("更新商户信息的时候出现了异常", e);
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
