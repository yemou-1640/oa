package com.yemou.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yemou.oa.pojo.Permission;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.PermissionService;
import com.yemou.oa.service.UserService;
import com.yemou.oa.utils.AjaxResult;
import com.yemou.oa.utils.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;
    
    // 跳转到权限列表页面
    @GetMapping("/toPermission")
    public String toPermissionList() {
        System.out.println("------toPermissionList------");
        return "admin/permission/list";
    }

    // 跳转到添加权限页面
    @GetMapping("/toAdd")
    public String toAdd() {
        System.out.println("------toPermissionAdd------");
        return "admin/permission/add";
    }

    // 跳转到编辑权限页面
    @GetMapping("/toEdit/{uid}")
    public String toEdit(@PathVariable Integer uid, HttpServletRequest request) {
        System.out.println("------toPermissionEdit------");
        System.out.println("uid:" + uid);
        
        // 获取用户信息
        User user = userService.getById(uid);
        if (user == null) {
            return "error/404";
        }
        request.setAttribute("user", user);
        
        // 获取当前用户的权限信息
        Permission permission = permissionService.getById(user.getPid());
        request.setAttribute("permission", permission);
        
        return "admin/permission/edit";
    }
    
    // 获取权限列表数据（分页）
    @GetMapping("/getList")
    @ResponseBody
    public PageResult<User> getList(String pname,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int limit) {
        System.out.println("------permission:getList------");
        System.out.println("pname:" + pname);
        System.out.println("page:" + page);
        System.out.println("limit:" + limit);
        if ("".equals(pname)) {
            pname=null;
        }
        PageResult<User> result=permissionService.getListByPage(pname, page, limit);
        return result;
    }
    
    // 添加权限
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(@RequestBody Permission permission) {
        System.out.println("------addPermission------");
        System.out.println("permission:" + permission);
        boolean success = permissionService.save(permission);
        if (success) {
            return AjaxResult.right();
        } else {
            return AjaxResult.error("添加权限失败");
        }
    }
    
    // 更新权限
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(@RequestBody Permission permission) {
        System.out.println("------updatePermission------");
        System.out.println("permission:" + permission);
        
        // 根据权限名称查找权限记录
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pname", permission.getPname());
        Permission targetPermission = permissionService.getOne(queryWrapper);
        
        if (targetPermission == null) {
            return AjaxResult.error("未找到对应的权限记录");
        }
        
        // 更新用户的权限ID
        User user = userService.getById(permission.getUid());
        if (user == null) {
            return AjaxResult.error("未找到对应的用户");
        }
        
        // 更新用户的权限ID
        user.setPid(targetPermission.getPid());
        boolean success = userService.updateById(user);
        
        if (success) {
            return AjaxResult.right();
        } else {
            return AjaxResult.error("更新权限失败");
        }
    }
    
    // 删除权限
    @PostMapping("/delete/{pid}")
    @ResponseBody
    public AjaxResult delete(@PathVariable Integer pid) {
        System.out.println("------deletePermission------");
        System.out.println("pid:" + pid);
        boolean success = permissionService.removeById(pid);
        if (success) {
            return AjaxResult.right();
        } else {
            return AjaxResult.error("删除权限失败");
        }
    }
    
    // 获取所有权限列表
    @GetMapping("/getAllPermissions")
    @ResponseBody
    public AjaxResult getAllPermissions() {
        System.out.println("------getAllPermissions------");
        List<Permission> permissions = permissionService.list();
        return AjaxResult.right().put("data", permissions).put("code", 0);
    }
}
