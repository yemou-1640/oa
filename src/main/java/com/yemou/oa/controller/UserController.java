package com.yemou.oa.controller;


import com.yemou.oa.pojo.Permission;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.PermissionService;
import com.yemou.oa.service.UserService;
import com.yemou.oa.utils.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    //跳转到登录页面
    @RequestMapping("/toLogin")
    public String toLogin(HttpServletRequest request){
        System.out.println("-----toLogin-----");
        //查询所有用户
        List<Permission> list=permissionService.list();
        HttpSession session=request.getSession();
        request.setAttribute("list",list);
        return "user/login";
    }

    //处理用户登录
//    @Operation(summary="处理用户登录",description="参数包含用户名，密码，角色主键")
    @RequestMapping("/login")
    @ResponseBody
    public AjaxResult login(User user, HttpSession session){
        System.out.println("-----login-----");
        System.out.println("user:"+user);
        //根据用户名，密码，角色查询指定用户
        User user2=userService.login(user);
        if(user2!=null){//登录成功
            //将当前登录的用户信息保存到session中
            session.setAttribute("user2",user2);
            
            // 获取用户权限信息，确定要跳转到哪个页面
            Integer permissionId = user.getPid();
            
            // 根据用户权限判断是普通用户还是管理员
            if(permissionId != null) {
                // 超级管理员(pid=1)和管理员(pid=2)都跳转到管理员端首页
                if(permissionId == 1 || permissionId == 2) {
                    // 管理员用户，存储角色标记并返回特定的code
                    session.setAttribute("isAdmin", true);
                    AjaxResult result = AjaxResult.right();
                    result.put("isAdmin", true);
                    return result;
                }
            }
            
            // 普通用户
            session.setAttribute("isAdmin", false);
            AjaxResult result = AjaxResult.right();
            result.put("isAdmin", false);
            return result;
        }else{//登录失败
            return AjaxResult.error();
        }
    }
    //退出登录
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        System.out.println("-----logout-----");
        //使session失效
        session.invalidate();
        //重定向到登陆页面
        return "redirect:/user/toLogin";
    }
    
    // 跳转到用户基本资料页面
    @RequestMapping("/toUserInfo")
    public String toUserInfo(){
        System.out.println("-----toUserInfo-----");
        return "user/info";
    }
    
    // 更新用户基本资料
    @RequestMapping(value = "/editInfo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateInfo(@RequestBody User user, HttpSession session){
        System.out.println("-----updateInfo-----");
        System.out.println("接收到的用户数据：" + user);
        
        try {
            User currentUser = (User) session.getAttribute("user2");
            if(currentUser == null){
                return AjaxResult.error("用户未登录");
            }
            
            // 只允许修改部分信息，保持ID不变
            user.setUid(currentUser.getUid());
            
            // 保持原有的一些字段不变
            user.setPassword(currentUser.getPassword());
            user.setPid(currentUser.getPid());
            user.setCreated(currentUser.getCreated());
            
            System.out.println("准备更新的用户数据：" + user);
            
            boolean success = userService.updateById(user);
            if(success){
                // 更新session中的用户信息
                User updatedUser = userService.getById(currentUser.getUid());
                session.setAttribute("user2", updatedUser);
                return AjaxResult.right();
            }else{
                return AjaxResult.error("更新失败，请检查数据是否正确");
            }
        } catch (Exception e) {
            System.err.println("更新用户信息时发生错误：" + e.getMessage());
            e.printStackTrace();
            return AjaxResult.error("系统错误：" + e.getMessage());
        }
    }

    //获取所有用户列表
    @GetMapping("/getList")
    @ResponseBody
    public AjaxResult getList() {
        try {
            List<User> users = userService.list();
            return AjaxResult.right().put("data", users);
        } catch (Exception e) {
            return AjaxResult.error("获取用户列表失败：" + e.getMessage());
        }
    }
}
