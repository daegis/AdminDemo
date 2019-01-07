package com.hnair.wallet.admincenter.web;

import com.alibaba.fastjson.JSON;
import com.hnair.wallet.admincenter.executor.OperatorListExecutor;
import com.hnair.wallet.admincenter.model.AdmincenterAuthority;
import com.hnair.wallet.admincenter.model.AdmincenterOperator;
import com.hnair.wallet.admincenter.model.AdmincenterRole;
import com.hnair.wallet.admincenter.noused.Merchant;
import com.hnair.wallet.admincenter.service.IMerchantService;
import com.hnair.wallet.admincenter.service.IOperatorService;
import com.hnair.wallet.admincenter.utils.ParamsUtils;
import com.hnair.wallet.admincenter.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Using IntelliJ IDEA.
 *
 * @author 李小鑫 at 2018/7/24 16:46
 */
@Controller
@Slf4j
@RequestMapping(value = "/operator")
@PreAuthorize("hasAuthority('operator')")
public class OperatorController extends BaseController {

    @Autowired
    IOperatorService operatorService;

    @Autowired
    IMerchantService merchantService;

    /**
     * 添加管理员
     *
     * @return
     */
    @RequestMapping("/addOperator")
    @ResponseBody
    public AJAXResult addOperator(AdmincenterOperator operator) {
        start();
        Integer password = new Random().nextInt(900000) + 100000;
        operator.setPassword(password.toString());
        AdmincenterOperator res = new AdmincenterOperator();
        res.setOperatorName(operator.getOperatorName());
        res.setPassword(password.toString());
        try {
            Integer addCount = operatorService.saveAdmincenterOperator(operator);
            if (addCount == 1) {
                data(res);
                success();
            } else fail();
        } catch (Exception e) {
            fail();
            log.info("增加管理员时出现异常！");
        }
        return end();
    }

    /**
     * 进入添加管理员页面
     *
     * @return
     */
    @RequestMapping("/addOperatorIndex")
    public String addOperatorIndex(ModelMap map) {
        List<Merchant> merchantList = new LinkedList<>();
        Merchant merchant = new Merchant();
        merchant.setId(1);
        merchant.setMerchantId("hnair");
        merchant.setMerchantName("海南航空");
        merchantList.add(merchant);
        map.addAttribute("merchantList", merchantList);
        return "wallet/operator/addoperatorindex";
    }

    /**
     * 重置管理员密码
     *
     * @return
     */
    @RequestMapping("/resetPassword/{id}")
    @ResponseBody
    public AJAXResult resetPassword(@PathVariable("id") Integer id) {
        start();
        Integer password = new Random().nextInt(900000) + 100000;
        try {
            Integer updateCount = operatorService.resetPassword(id, password.toString());
            if (updateCount == 1) {
                success();
                data(password);
            } else fail();
        } catch (Exception e) {
            log.error("为operatorId为" + id + "的管理员重置密码时出现异常！", e);
            fail();
        }
        return end();
    }

    /**
     * 查看管理员列表
     *
     * @param map
     * @return
     */
    @RequestMapping("/operatorList")
    public String getAuthorityOperatorList(ModelMap map, AdmincenterOperatorVo requVo, Integer pageNo, Integer pageSize) {
        Map<String, Object> queryMap = new HashMap<>();
        ParamsUtils.getAllProperties(requVo, queryMap);
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
//            Future<Object> submit2 = executorService.submit(new OperatorListExecutor(merchantService, 3));
//            Future<Object> submit1 = executorService.submit(new OperatorListExecutor(operatorService, queryMap, 2));
            Future<Object> submit = executorService.submit(new OperatorListExecutor(operatorService, queryMap, pageNo, pageSize, 1));
            List<Merchant> o2 = new ArrayList<>();
            Merchant merchant = new Merchant();
            merchant.setId(100);
            merchant.setMerchantId("hnair");
            merchant.setMerchantName("海南航空");
            o2.add(merchant);
            Integer o1 = 10;
            List<AdmincenterOperatorVo> o = new LinkedList<>();
            OperatorListResponseVo responseVo = new OperatorListResponseVo(pageNo, pageSize, o1, o, requVo, o2);
            map.addAttribute("result", responseVo);
        } catch (Exception e) {
            log.error("查询管理员列表出现异常！", e);
        } finally {
            executorService.shutdown();
        }
        return "wallet/operator/operatorlist";
    }

    /**
     * 查看管理员角色详情
     *
     * @param map
     * @param id
     * @return
     */
    @RequestMapping("/authority/roleInfo/{id}")
    public String viewOperatorRoleInfo(ModelMap map, @PathVariable(value = "id") Integer id) {
        //查出当前管理员的角色
        Set<AdmincenterRole> roleList = operatorService.viewOperatorRoleInfoByOperatorId(id);
        //查出所有管理员的角色
        List<AdmincenterRole> allRoleList = operatorService.getAdmincenterRoleList();
        allRoleList.removeAll(roleList);
        map.addAttribute("assignList", roleList);
        map.addAttribute("unassignList", allRoleList);
        //查出管理员的详细信息
        AdmincenterOperatorVo admincenterOperatorVo = operatorService.operatorDetail(id);
        map.addAttribute("operatorVo", admincenterOperatorVo);
        return "wallet/operator/assignrole";
    }

    /**
     * 增加管理员角色
     *
     * @param operatorId
     * @param dataVo
     * @return
     */
    @RequestMapping("/assignRoles")
    @ResponseBody
    public AJAXResult assignRoles(Integer operatorId, DataVo dataVo) {
        start();
        try {
            //为每个管理员进行操作
            Integer addCount = operatorService.assignRoles(operatorId, dataVo.getIds());
            if (addCount == dataVo.getIds().size()) success();
            else fail();
        } catch (Exception e) {
            log.error("为operatorId为" + operatorId + "的管理员增加角色出现异常", e);
            fail();
        }
        return end();
    }

    /**
     * 减少管理员角色
     *
     * @param operatorId
     * @param dataVo
     * @return
     */
    @RequestMapping("/unAssignRoles")
    @ResponseBody
    public AJAXResult unAssignRoles(Integer operatorId, DataVo dataVo) {
        start();
        try {
            //为每个管理员进行操作
            Integer deleteCount = operatorService.unAssignRoles(operatorId, dataVo.getIds());
            if (deleteCount == dataVo.getIds().size()) success();
            else fail();
        } catch (Exception e) {
            log.error("为operatorId为" + operatorId + "的管理员减少角色出现异常", e);
            fail();
        }
        return end();
    }

    /**
     * 删除管理员
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AJAXResult deleteOperator(Integer id) {
        start();
        try {
            //为每个管理员进行操作
            Integer deleteCount = operatorService.deleteOperator(id);
            if (deleteCount == 1) success();
            else fail();
        } catch (Exception e) {
            log.error("删除operatorId为" + id + "的管理员出现异常", e);
            fail();
        }
        return end();
    }

    /**
     * 角色列表
     *
     * @return
     */
    @RequestMapping("/roleList")
    public String roleList(ModelMap map, Integer pageNo, Integer pageSize) {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("pageNo", pageNo);
        reqMap.put("pageSize", pageSize);
        List<AdmincenterRole> roleList = operatorService.getAllAdmincenterRoleOnPage(reqMap);
        Integer totalCount = operatorService.getAdmincenterRolesTotalCount();
        map.addAttribute("pageNo", pageNo);
        map.addAttribute("pageSize", pageSize);
        map.addAttribute("totalCount", totalCount);
        map.addAttribute("roleList", roleList);
        return "wallet/operator/rolelist";
    }

    /**
     * 进入权限页面
     *
     * @return
     */
    @RequestMapping("/authorityList")
    public String authorityList() {
        return "wallet/operator/authoritytree";
    }

    /**
     * 加载权限数据
     *
     * @return
     */
    @RequestMapping("/authority/loadData")
    @ResponseBody
    public Object loadData() {
        List<Permission> permissions = new ArrayList<>();
        Map<Integer, Permission> permissionMap = new HashMap<>();
        //查询出所有的权限
        List<Permission> allPermissions = operatorService.queryAllAuthority();
        //将所有的权限放到map中
        allPermissions.stream().filter(o -> o != null).forEach(o -> {
            permissionMap.put(o.getId(), o);
        });
        //组合父子权限
        allPermissions.stream().filter(o -> o != null).forEach(o -> {
            if (o.getPid() == 0) {
                //根节点操作
                permissions.add(o);
            } else {
                //获取父权限并且组合权限之间的关系
                permissionMap.get(o.getPid()).getChildren().add(o);
            }
        });
        return permissions;
    }

    /**
     * 增加权限页面跳转
     *
     * @return
     */
    @RequestMapping("/addAuthorityIndex/{id}")
    public String addAuthorityIndex(ModelMap map, @PathVariable("id") Integer id) {

        map.put("pid", id);
        return "wallet/operator/addauthority";
    }

    /**
     * 增加权限
     *
     * @return
     */
    @RequestMapping("/addAuthority")
    @ResponseBody
    public AJAXResult addAuthority(AdmincenterAuthority admincenterAuthority) {
        start();
        try {
            Integer addCount = operatorService.saveAdmincenterAuthority(admincenterAuthority);
            if (addCount == 1) success();
            else fail();
        } catch (Exception e) {
            log.error("增加权限出现异常!", e);
            fail();
        }
        return end();
    }

    /**
     * 跳转到角色权限页面
     *
     * @return
     */
    @RequestMapping("/roleAuthorityIndex/{roleId}")
    public String operatorAuthorityIndex(ModelMap map, @PathVariable("roleId") Integer roleId) {
        AdmincenterRole role = operatorService.getRoleInfoByRoleId(roleId);
        map.put("role", role);
        return "wallet/operator/authorityInfo";
    }

    @ResponseBody
    @RequestMapping("/loadRoleAuthority/{roleId}")
    public Object loadRoleAuthority(@PathVariable("roleId") Integer roleid) {
        List<Permission> permissions = new ArrayList<>();
        // 获取用户的权限数据（未拼装）
        List<Integer> permissionIds = operatorService.queryPermissionIdsByRoleid(roleid);
        // 查询所有的权限
        List<Permission> allPermissions = operatorService.queryAllAuthority();
        Map<Integer, Permission> permissionMap = new HashMap<>();
        //将所有的权限放到map中
        allPermissions.stream().filter(o -> o != null).forEach(o -> {
            if (permissionIds.contains(o.getId())) {
                // 已分配, 选中
                o.setChecked(true);
            }
            permissionMap.put(o.getId(), o);
        });
        //组合父子权限
        allPermissions.stream().filter(o -> o != null).forEach(o -> {
            if (o.getPid() == 0) {
                //根节点操作
                permissions.add(o);
            } else {
                //获取父权限并且组合权限之间的关系
                permissionMap.get(o.getPid()).getChildren().add(o);
            }
        });
        return permissions;
    }


    /**
     * 为角色分配权限
     *
     * @param roleid
     * @param ds
     * @return
     */
    @ResponseBody
    @RequestMapping("/assignAuthority")
    public AJAXResult assignAuthority(Integer roleId, DataVo ds) {
        start();
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("roleId", roleId);
            paramMap.put("authorityIds", ds.getIds());
            Integer integer = operatorService.assignAuthority2Role(paramMap);
            if (integer == 1) success();
            else fail();
        } catch (Exception e) {
            log.error("为roleId为：{}分配权限出现异常!", roleId, e);
            fail();
        }
        return end();
    }


    /**
     * 修改权限页面跳转
     *
     * @param map
     * @param id
     * @return
     */
    @RequestMapping("/updateAuthorityIndex/{id}")
    public String updateAuthorityIndex(ModelMap map, @PathVariable("id") Integer id) {
        AdmincenterAuthority admincenterAuthority = operatorService.getAuthorityInfoByAuthorityId(id);
        map.put("authority", admincenterAuthority);
        return "wallet/operator/editauthority";
    }

    /**
     * 修改权限信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateAuthority")
    public AJAXResult updateAuthority(AdmincenterAuthority admincenterAuthority) {
        start();
        try {
            Integer updateCount = operatorService.updateAuthority(admincenterAuthority);
            if (updateCount == 1) success();
            else fail();
        } catch (Exception e) {
            log.error("修改权限信息异常:修改实体为：{}", JSON.toJSONString(admincenterAuthority), e);
            fail();
        }
        return end();
    }

    /**
     * 删除权限树中的权限
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/authority/delete")
    public AJAXResult deleteAuthority(Integer id) {
        start();
        try {
            Integer deleteCount = operatorService.deleteAuthorityById(id);
            if (deleteCount == 1) success();
            else fail();
        } catch (Exception e) {
            log.error("删除权限树中的权限出现异常:删除id为：{}", id, e);
            fail();
        }
        return end();
    }

    /**
     * 增加角色跳转
     *
     * @return
     */
    @RequestMapping("/addRoleIndex")
    public String addRoleIndex() {
        return "wallet/operator/addrole";
    }

    /**
     * 增加角色
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/addRole")
    public AJAXResult addRole(AdmincenterRole role, DataVo vo) {
        start();
        try {
            Integer addCount = operatorService.addRole(role, vo);
            if (addCount == 1) success();
            else fail();
        } catch (Exception e) {
            fail();
            log.error("增加角色出现异常！", e);
        }
        return end();
    }

    /**
     * 删除角色
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteRole/{roleId}")
    public AJAXResult deleteRole(@PathVariable("roleId") Integer roleId) {
        start();
        try {
            Integer deleteCount = operatorService.deleteRoleByRoleId(roleId);
            if (deleteCount == 1) success();
            else fail();
        } catch (Exception e) {
            fail();
            log.error("删除角色出现异常！", e);
        }
        return end();
    }
}
