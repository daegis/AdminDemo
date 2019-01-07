package com.hnair.wallet.admincenter.service;

import com.hnair.wallet.admincenter.model.AdmincenterAuthority;
import com.hnair.wallet.admincenter.model.AdmincenterOperator;
import com.hnair.wallet.admincenter.model.AdmincenterRole;
import com.hnair.wallet.admincenter.vo.AdmincenterOperatorVo;
import com.hnair.wallet.admincenter.vo.DataVo;
import com.hnair.wallet.admincenter.vo.Permission;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Using IntelliJ IDEA.
 *
 * @author 李小鑫 at 2018/7/24 16:47
 */
public interface IOperatorService {

    List<AdmincenterOperatorVo> getAuthorityOperatorList(Map<String, Object> queryMap, Integer pageNo, Integer pageSize);

    AdmincenterOperatorVo operatorDetail(Integer id);

    Set<AdmincenterRole> viewOperatorRoleInfoByOperatorId(Integer id);

    List<AdmincenterRole> getAdmincenterRoleList();

    Integer assignRoles(Integer operatorId, List<Integer> ids);

    Integer unAssignRoles(Integer operatorId, List<Integer> ids);

    Integer deleteOperator(Integer operatorId) throws Exception;

    Integer getAllOperatorCount(Map<String, Object> queryMap);

    Integer resetPassword(Integer id, String password);

    Integer saveAdmincenterOperator(AdmincenterOperator operator) throws Exception;

    List<AdmincenterRole> getAllAdmincenterRoleOnPage(Map<String, Object> reqMap);

    Integer getAdmincenterRolesTotalCount();

    List<Permission> queryAllAuthority();

    Integer saveAdmincenterAuthority(AdmincenterAuthority admincenterAuthority);

    List<Integer> queryPermissionIdsByRoleid(Integer roleid);

    AdmincenterRole getRoleInfoByRoleId(Integer roleId);

    Integer assignAuthority2Role(Map<String, Object> paramMap) throws Exception;

    AdmincenterAuthority getAuthorityInfoByAuthorityId(Integer id);

    Integer updateAuthority(AdmincenterAuthority admincenterAuthority);

    Integer deleteAuthorityById(Integer id);

    Integer addRole(AdmincenterRole role, DataVo vo) throws Exception;

    Integer deleteRoleByRoleId(Integer roleId) throws Exception;
}
