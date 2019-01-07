package com.hnair.wallet.admincenter.service.impl;

import cn.aegisa.selext.dao.service.ICommonService;
import com.hnair.wallet.admincenter.constant.RoleTypeEnum;
import com.hnair.wallet.admincenter.model.*;
import com.hnair.wallet.admincenter.service.IOperatorService;
import com.hnair.wallet.admincenter.vo.AdmincenterOperatorVo;
import com.hnair.wallet.admincenter.vo.DataVo;
import com.hnair.wallet.admincenter.vo.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Using IntelliJ IDEA.
 *
 * @author 李小鑫 at 2018/7/24 16:47
 */
@Service
@Slf4j
public class OperatorServiceImpl implements IOperatorService {

    @Autowired
    private ICommonService commonService;

    @Override
    public List<AdmincenterOperatorVo> getAuthorityOperatorList(Map<String, Object> queryMap, Integer pageNo, Integer pageSize) {
        if (pageNo == null && pageSize == null) {
            pageNo = 1;
            pageSize = 10;
        }
        queryMap.put("startIndex", (pageNo - 1) * pageSize);
        queryMap.put("pageSize", pageSize);
        List<AdmincenterOperatorVo> list = commonService.getListBySqlId(AdmincenterOperator.class, "getAdmincenterOperatorWithMerchant", queryMap);
        return list;
    }

    @Override
    public AdmincenterOperatorVo operatorDetail(Integer id) {
        AdmincenterOperatorVo admincenterOperator = commonService.getBySqlId(AdmincenterOperator.class, "operatorDetail", "operatorId", id);
        return admincenterOperator;
    }

    @Override
    public Set<AdmincenterRole> viewOperatorRoleInfoByOperatorId(Integer id) {
        List<AdmincenterRole> roleList = commonService.getListBySqlId(AdmincenterRole.class, "queryOperatorRolesByOperatorId", "operatorId", id);
        if (!CollectionUtils.isEmpty(roleList)) {
            TreeSet<AdmincenterRole> admincenterRoles = new TreeSet<>(new Comparator<AdmincenterRole>() {
                @Override
                public int compare(AdmincenterRole o1, AdmincenterRole o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
            admincenterRoles.addAll(roleList);
            return admincenterRoles;
        }
        return null;
    }

    @Override
    public List<AdmincenterRole> getAdmincenterRoleList() {
        List<AdmincenterRole> roleList = commonService.getList(AdmincenterRole.class);
        return roleList;
    }

    @Override
    public Integer assignRoles(Integer operatorId, List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        List<AdmincenterOperatorRole> list = new ArrayList<>();
        try {
            ids.stream().filter(o -> o != null).forEach(o -> {
                AdmincenterOperatorRole admincenterOperatorRole = new AdmincenterOperatorRole();
                admincenterOperatorRole.setOperatorId(operatorId);
                admincenterOperatorRole.setRoleId(o);
                list.add(admincenterOperatorRole);
            });
            int save = commonService.save(AdmincenterOperatorRole.class, "assignRole", "list", list);
            return save;
        } catch (Exception e) {
            log.error("为operatorId为{}的管理员添加权限时异常", operatorId, e);
            return 0;
        }
    }

    @Override
    public Integer unAssignRoles(Integer operatorId, List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        try {
            int save = commonService.deleteBySqlId(AdmincenterOperatorRole.class, "unAssignRoles", "operatorId", operatorId, "list", ids);
            return save;
        } catch (Exception e) {
            log.error("为operatorId为{}的管理员删除权限时异常", operatorId, e);
            return 0;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer deleteOperator(Integer operatorId) throws Exception {
        if (operatorId == null) {
            return 0;
        }
        //删除管理员
        int delete = commonService.deleteBySqlId(AdmincenterOperator.class, "deleteByPrimaryKey", "id", operatorId);
        //删除管理员角色
        int delete1 = commonService.deleteBySqlId(AdmincenterOperatorRole.class, "deleteByOperatorId", "operatorId", operatorId);
        if (delete == 1 && delete1 > 0) {
            return 1;
        } else {
            throw new Exception("删除管理员失败！");
        }
    }

    @Override
    public Integer getAllOperatorCount(Map<String, Object> queryMap) {
        return commonService.getBySqlId(AdmincenterOperator.class, "getAllOperatorCountWithCondition", queryMap);
    }

    @Override
    public Integer resetPassword(Integer id, String password) {
        AdmincenterOperator admincenterOperator = commonService.get(AdmincenterOperator.class, "id", id);
        admincenterOperator.setPassword(getCipherText(password.concat(admincenterOperator.getOperatorUniqueSalt())));
        return commonService.update(admincenterOperator);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer saveAdmincenterOperator(AdmincenterOperator operator) throws Exception {
        //为用户增加随机密码
        operator.setOperatorUniqueSalt(getCipherText(UUID.randomUUID().toString()));
        operator.setPassword(getCipherText(operator.getPassword().concat(operator.getOperatorUniqueSalt())));
        int save = commonService.save(operator);
        //为新创建管理员增加最低权限
        AdmincenterOperatorRole operatorRole = new AdmincenterOperatorRole();
        operatorRole.setRoleId(RoleTypeEnum.MERCHANT_READ.getType());
        operatorRole.setOperatorId(operator.getId());
        int save1 = commonService.save(operatorRole);
        if (save == 1 && save1 == 1) {
            return 1;
        } else {
            throw new Exception("增加管理员失败！");
        }
    }

    @Override
    public List<AdmincenterRole> getAllAdmincenterRoleOnPage(Map<String, Object> reqMap) {
        Integer pageNo = (Integer) reqMap.get("pageNo");
        Integer pageSize = (Integer) reqMap.get("pageSize");
        if (pageNo == null) {
            pageNo = 1;
            reqMap.put("pageNo", pageNo);
        }
        if (pageSize == null) {
            pageSize = 10;
            reqMap.put("pageSize", pageSize);
        }
        reqMap.put("startIndex", (pageNo - 1) * pageSize);
        return commonService.getListBySqlId(AdmincenterRole.class, "getAllAdmincenterRoleOnPage", reqMap);
    }

    @Override
    public Integer getAdmincenterRolesTotalCount() {
        return commonService.getBySqlId(AdmincenterRole.class, "pageCount");
    }

    @Override
    public List<Permission> queryAllAuthority() {
        return commonService.getListBySqlId(AdmincenterAuthority.class, "queryAllAuthority");
    }

    @Override
    public Integer saveAdmincenterAuthority(AdmincenterAuthority admincenterAuthority) {
        //11个图标 随机分配
        String[] icons = new String[]{"glyphicon glyphicon-th", "glyphicon glyphicon-heart", "glyphicon glyphicon-align-left",
                "glyphicon glyphicon-equalizer", "glyphicon glyphicon-stats", "glyphicon glyphicon-hdd", "glyphicon glyphicon-folder-close",
                "glyphicon glyphicon-leaf", "glyphicon glyphicon-send", "glyphicon glyphicon-menu-hamburger", "glyphicon glyphicon-grain"};
        int i = new Random().nextInt(icons.length);
        admincenterAuthority.setIcon(icons[i]);
        return commonService.save(admincenterAuthority);
    }

    @Override
    public List<Integer> queryPermissionIdsByRoleid(Integer roleId) {
        Map<Integer, AdmincenterAuthority> allAuthorityMap = new HashMap<>();
        //查询出所有的权限
        List<AdmincenterAuthority> allAuthority = commonService.getList(AdmincenterAuthority.class);
        //放入到map中
        for (AdmincenterAuthority admincenterAuthority : allAuthority) {
            allAuthorityMap.put(admincenterAuthority.getPid(), admincenterAuthority);
        }
        //根据pid进行分组
        Map<Integer, List<AdmincenterAuthority>> authorityPidMap = allAuthority.stream().collect(Collectors.groupingBy(AdmincenterAuthority::getPid));
        Set<Integer> authorityIdSet = new HashSet<>();
        //获取当前角色的权限id集合
        List<Integer> ids = commonService.getListBySqlId(AdmincenterRoleAuthority.class, "queryAuthorityIdsByRoleid", "roleId", roleId);
        //递归获取所有的权限id集合
        for (Integer aythorityId : ids) {
            authorityIdSet.add(aythorityId);
            if (authorityPidMap.containsKey(aythorityId)) {
                convertAuthority(authorityIdSet, aythorityId, authorityPidMap);
            }
        }
        return new ArrayList<>(authorityIdSet);
    }

    @Override
    public AdmincenterRole getRoleInfoByRoleId(Integer roleId) {
        return commonService.get(AdmincenterRole.class, "id", roleId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer assignAuthority2Role(Map<String, Object> paramMap) throws Exception {
        Integer roleId = (Integer) paramMap.get("roleId");
        List<Integer> authorityIds = (List<Integer>) paramMap.get("authorityIds");
        if (roleId != null && !CollectionUtils.isEmpty(authorityIds)) {
            List<AdmincenterRoleAuthority> admincenterRoleAuthorities = new ArrayList<>();
            authorityIds.stream().filter(o -> o != null).forEach(o -> {
                AdmincenterRoleAuthority admincenterRoleAuthority = new AdmincenterRoleAuthority();
                admincenterRoleAuthority.setRoleId(roleId);
                admincenterRoleAuthority.setAuthorityId(o);
                admincenterRoleAuthorities.add(admincenterRoleAuthority);
            });
            List<AdmincenterRoleAuthority> authorities = commonService.getList(AdmincenterRoleAuthority.class, "roleId", roleId);
            List<Integer> roleIds = new ArrayList<>();
            authorities.stream().forEach(o -> {
                roleIds.add(o.getId());
            });
            int delete = 0;
            if (!CollectionUtils.isEmpty(roleIds)) {
                delete = commonService.deleteBySqlId(AdmincenterRoleAuthority.class, "deleteByRoleIds", "roleIds", roleIds);
            }
            if (delete == authorities.size()) {
                int save = commonService.save(AdmincenterRoleAuthority.class, "saveAuthority2Role", "list", admincenterRoleAuthorities);
                if (save > 0) {
                    return 1;
                } else {
                    throw new Exception("事务提交异常！数据回滚");
                }
            } else {
                throw new Exception("事务提交异常！数据回滚");
            }
        } else {
            return 0;
        }
    }

    @Override
    public AdmincenterAuthority getAuthorityInfoByAuthorityId(Integer id) {
        return commonService.get(AdmincenterAuthority.class, "id", id);
    }

    @Override
    public Integer updateAuthority(AdmincenterAuthority admincenterAuthority) {
        return commonService.update(admincenterAuthority);
    }

    @Override
    public Integer deleteAuthorityById(Integer id) {
        if (id == null || id < 0) {
            return 0;
        }
        return commonService.deleteBySqlId(AdmincenterAuthority.class, "deleteAuthorityById", "id", id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer addRole(AdmincenterRole role, DataVo vo) throws Exception {
        int save = commonService.save(role);
        if (save == 1 && role.getId() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("roleId", role.getId());
            map.put("authorityIds", vo.getIds());
            Integer integer = assignAuthority2Role(map);
            return integer;
        } else {
            throw new Exception("事务提交异常！数据回滚");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer deleteRoleByRoleId(Integer roleId) throws Exception{
        int delteCount = commonService.deleteBySqlId(AdmincenterRole.class, "deleteRoleByRoleId", "roleId", roleId);
        if(delteCount != 1){
            throw new Exception("删除角色异常，数据回滚！");
        }
        return delteCount;
    }

    private void convertAuthority(Set<Integer> authorityIdSet, Integer id, Map<Integer, List<AdmincenterAuthority>> allAuthorityMap) {
        List<AdmincenterAuthority> authorityList = allAuthorityMap.get(id);
        if (!CollectionUtils.isEmpty(authorityList)) {
            for (AdmincenterAuthority authority : authorityList) {
                authorityIdSet.add(authority.getId());
                convertAuthority(authorityIdSet, authority.getId(), allAuthorityMap);
            }
        }
    }

    private String getCipherText(String plainText) {
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            sha256Digest.update(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] digestByteArray = sha256Digest.digest();
            final byte[] base64ByteArray = Base64.getEncoder().encode(digestByteArray);
            return DatatypeConverter.printHexBinary(base64ByteArray);
        } catch (Exception ignored) {
        }
        return null;
    }
}
