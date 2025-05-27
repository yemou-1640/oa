package com.yemou.oa.controller;

import com.yemou.oa.pojo.Permission;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.*;
import com.yemou.oa.utils.AjaxResult;
import com.yemou.oa.utils.PageResult;
import com.yemou.oa.pojo.Department;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 管理员控制器
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PendingService pendingService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private CompletedService completedService;
    @Autowired
    private PermissionService permissionService;

    //跳转到用户列表页面
    @GetMapping("/user/toList")
    public String toUserList() {
        System.out.println("------toUserList------");
        return "admin/user/list";
    }

    //跳转编辑
    @GetMapping("/user/toEdit/{uid}")
    public String toUserEdit(@PathVariable Integer uid, HttpServletRequest request) {
        System.out.println("------toUserEdit------");
        System.out.println("uid:" + uid);
        User user=userService.getById(uid);
        System.out.println("完整用户对象: " + user);
        request.setAttribute("user", user);
        return "admin/user/edit";
    }

    //跳转到添加用户页面
    @GetMapping("/user/toAdd")
    public String toUserAdd() {
        System.out.println("------toUserAdd------");
        return "admin/user/add";
    }
    //待办审批
    @GetMapping("/toPending")
    public String toPending(Model model) {
        System.out.println("------toPending------");
        // 获取待审批的请假申请数量
        int leaveCount = pendingService.getPendingLeaveCount();

        model.addAttribute("leaveCount", leaveCount);

        // 获取待审批的出差申请数量
        int businessCount = pendingService.getPendingBusinessCount();
        model.addAttribute("businessCount", businessCount);

        // 获取待审批的报销申请数量
        int reimbursementCount = pendingService.getPendingReimbursementCount();
        model.addAttribute("reimbursementCount", reimbursementCount);

        return "admin/pending/pending";
    }
    //已办审批
    @GetMapping("/toCompleted")
    public String toCompleted(Model model) {
        System.out.println("------toCompleted------");
        // 获取各类已完成审批的数量
        int leaveCompletedCount = completedService.getCompletedLeaveCount();
        int businessCompletedCount = completedService.getCompletedBusinessCount();
        int reimbursementCompletedCount = completedService.getCompletedReimbursementCount();
        // 将数据添加到模型中
        model.addAttribute("leaveCompletedCount", leaveCompletedCount);
        model.addAttribute("businessCompletedCount", businessCompletedCount);
        model.addAttribute("reimbursementCompletedCount", reimbursementCompletedCount);

        return "admin/completed/com";
    }
    //用户管理
    @GetMapping("/user/getList")
    @ResponseBody
    public PageResult<User> getList(String name,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int limit) {
        System.out.println("------admin\\user:getList------");

        System.out.println("name:" + name);
        System.out.println("page:" + page);
        System.out.println("limit:" + limit);
        if("".equals(name)){
            name=null;
        }
        
        PageResult<User> result = userService.getListByPage(name, page, limit);
        return result;
    }
    
    //获取所有部门列表
    @GetMapping("/department/list")
    @ResponseBody
    public AjaxResult getDepartmentList() {
        System.out.println("------admin\\user:getDepartmentList------");
        List<Department> dept = departmentService.list();
        System.out.println("dept:" + dept);
        return AjaxResult.right().put("data", dept).put("code", 0);
    }

    //获取所有权限列表
    @GetMapping("/permission/list")
    @ResponseBody
    public AjaxResult getPermissionList() {
        System.out.println("------admin\\user:getPermissionList------");
        List<Permission> permission = permissionService.list();
        System.out.println("permission:" + permission);
        return AjaxResult.right().put("data", permission).put("code", 0);
    }
    
    //添加用户
    @PostMapping("/user/add")
    @ResponseBody
    public AjaxResult addUser(@RequestBody User user) {
        try {
            // 设置创建时间
            user.setCreated(new Date());
            boolean saved = userService.save(user);
            
            if (saved) {
                return AjaxResult.right();
            } else {
                return AjaxResult.error("添加用户失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("系统异常：" + e.getMessage());
        }
    }
    
    //更新用户信息
    @PostMapping("/user/update")
    @ResponseBody
    public AjaxResult updateUser(@RequestBody User user) {
        System.out.println("----edit:user----");
        System.out.println("user:"+user);
        boolean flag=userService.updateById(user);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }
    
    //删除用户
    @PostMapping("/user/delete/{uid}")
    @ResponseBody
    public AjaxResult deleteUser(@PathVariable("uid") Integer uid) {
        System.out.println("----deleteUser------");
        try {
            boolean removed = userService.removeById(uid);
            
            if (removed) {
                return AjaxResult.right();
            } else {
                return AjaxResult.error("删除用户失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("系统异常：" + e.getMessage());
        }
    }
    

} 