package com.hnair.wallet.admincenter.web;

import com.alibaba.fastjson.JSON;
import com.hnair.wallet.admincenter.noused.Merchant;
import com.hnair.wallet.admincenter.service.IConsumeService;
import com.hnair.wallet.admincenter.service.IMerchantService;
import com.hnair.wallet.admincenter.vo.ConsumeListVo;
import com.hnair.wallet.admincenter.vo.DatagridResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/27/2018 10:21 AM
 */
@Controller
@Slf4j
@RequestMapping("/consume")
public class ConsumeController {

    @Autowired
    private IConsumeService consumeService;

    @Autowired
    private IMerchantService merchantService;

    /**
     * 跳转到消费列表界面
     *
     * @return
     */
    @RequestMapping("/list")
    public String consumeList(Model model) {
        List<Merchant> merchantList = merchantService.getAllMerchantList();
        model.addAttribute("merchantList", merchantList);
        return "wallet/consume/list";
    }

    /**
     * 消费列表页面异步请求接口，返回交易数据
     *
     * @param request
     * @return
     */
    @PostMapping("/getConsumeTradeList")
    @ResponseBody
    public DatagridResponse getConsumeTradeList(HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            System.out.println(authority.getAuthority());
        }
        // 判断是商户还是管理员   看有没有写的权限
        Map<String, String[]> map = request.getParameterMap();
        System.out.println(JSON.toJSONString(map));
        DatagridResponse<ConsumeListVo> response = new DatagridResponse<>();
        List<ConsumeListVo> list = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            ConsumeListVo vo = new ConsumeListVo();
            vo.setTradeNo("20180518220514634521");
            vo.setAccountType("返现");
            vo.setChannelOrderNo("HU1400052642");
            vo.setOrderStatus("已完成");
            vo.setOrderTime("2018-08-08 22:22:22");
            vo.setPayTime("2018-08-08 22:22:22");
            vo.setTradeBalance("555.55");
            vo.setUserId("HUA5466168");
            list.add(vo);
        }
        response.setData(list);
        response.setCode(0);
        response.setMsg("测试错误消息");
        return response;
    }


}
