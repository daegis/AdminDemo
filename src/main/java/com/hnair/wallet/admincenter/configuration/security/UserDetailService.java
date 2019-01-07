package com.hnair.wallet.admincenter.configuration.security;

import cn.aegisa.selext.dao.service.ICommonService;
import com.alibaba.fastjson.JSON;
import com.hnair.wallet.admincenter.model.AdmincenterAuthority;
import com.hnair.wallet.admincenter.model.AdmincenterOperator;
import com.hnair.wallet.admincenter.vo.AdmincenterOperatorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/22/2018 7:18 PM
 */
@Component
@Slf4j
public class UserDetailService implements UserDetailsService {

    @Autowired
    ICommonService commonService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //查询出所有的第一级权限
        List<AdmincenterOperatorVo> operatorList = commonService.getListBySqlId(AdmincenterOperator.class, "getOpreateorInfoWithAuthoruty", "name", s);
        if (CollectionUtils.isEmpty(operatorList)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        AdmincenterOperatorVo firstOperator = operatorList.listIterator().next();
        AdmincenterOperator admincenterOperator = new AdmincenterOperator();
        admincenterOperator.setOperatorName(firstOperator.getOperatorName());
        admincenterOperator.setOperatorNickName(firstOperator.getOperatorNickName());
        admincenterOperator.setPassword(firstOperator.getPassword());
        admincenterOperator.setOperatorUniqueSalt(firstOperator.getOperatorUniqueSalt());
        admincenterOperator.setOperatorSource(firstOperator.getOperatorSource());
        //权限去重
        Set<GrantedAuthority> admincenterAuthoritySet = new TreeSet<>((o1, o2) -> {
            AdmincenterAuthority a1 = (AdmincenterAuthority) o1;
            AdmincenterAuthority a2 = (AdmincenterAuthority) o2;
            if (a1.getAuthorityName() != null && a2.getAuthorityName() != null) {
                return a1.getAuthorityName().compareTo(a2.getAuthorityName());
            }
            return 0;
        });
        Map<Integer, AdmincenterAuthority> allAuthorityMap = new HashMap<>();
        //查询出所有的权限
        List<AdmincenterAuthority> allAuthority = commonService.getList(AdmincenterAuthority.class);
        //放到map中后面方便遍历
        for (AdmincenterAuthority admincenterAuthority : allAuthority) {
            allAuthorityMap.put(admincenterAuthority.getPid(), admincenterAuthority);
        }
        //根据父Id分组
        Map<Integer, List<AdmincenterAuthority>> authorityPidMap = allAuthority.stream().collect(Collectors.groupingBy(AdmincenterAuthority::getPid));
        //遍历递归查询出所有的权限
        for (AdmincenterOperatorVo operatorVo : operatorList) {
            AdmincenterAuthority admincenterAuthority = new AdmincenterAuthority();
            admincenterAuthority.setAuthorityName(operatorVo.getAuthorityName());
            admincenterAuthoritySet.add(admincenterAuthority);
            if (authorityPidMap.containsKey(operatorVo.getId())) {
                convertAuthority(admincenterAuthoritySet, operatorVo.getId(), authorityPidMap);
            }
        }
        admincenterOperator.setGrantedAuthoritySet(admincenterAuthoritySet);
        log.info("拥有的权限为：{}", JSON.toJSONString(admincenterAuthoritySet));
        return admincenterOperator;
    }

    private void convertAuthority(Set<GrantedAuthority> admincenterAuthoritySet, Integer id, Map<Integer, List<AdmincenterAuthority>> allAuthorityMap) {
        List<AdmincenterAuthority> authorityList = allAuthorityMap.get(id);
        if (!CollectionUtils.isEmpty(authorityList)) {
            for (AdmincenterAuthority authority : authorityList) {
                admincenterAuthoritySet.add(authority);
                convertAuthority(admincenterAuthoritySet, authority.getId(), allAuthorityMap);
            }
        }
    }
}
