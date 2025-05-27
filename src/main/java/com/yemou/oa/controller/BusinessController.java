package com.yemou.oa.controller;

import com.yemou.oa.pojo.Business;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.BusinessService;
import com.yemou.oa.service.UserService;
import com.yemou.oa.utils.AjaxResult;
import com.yemou.oa.utils.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yemou
 * @since 2025-04-27
 */
@Controller
@RequestMapping("/business")
public class BusinessController {
    @Autowired
    private BusinessService businessService;
    @Autowired
    private UserService userService;

    //跳转出差申请页面
    @GetMapping("/toBusiness")
    public String toBusiness() {
        return "business/add";
    }
    
    //跳转申请记录页面
    @GetMapping("/toList")
    public String toList() {
        return "business/list";
    }
    
    @GetMapping("/toEdit/{tid}")
    public String toEdit(@PathVariable("tid") Integer tid, HttpServletRequest request) {
        Business business = businessService.getById(tid);
        request.setAttribute("business", business);
        return "business/edit";
    }
    @GetMapping("/toPending")
    public String toPending() {
        return "admin/pending/business/bpending";
    }

    @GetMapping("/toApprove/{tid}")
    public String toApprove(@PathVariable("tid") Integer tid, HttpServletRequest request) {
        // 获取出差记录并加载用户信息
        Business business = businessService.getById(tid);
        if (business != null && business.getUid() != null) {
            // 加载用户信息
            User user = userService.getById(business.getUid());
            business.setUser(user);
        }
        request.setAttribute("business", business);
        return "admin/pending/business/bApprove";
    }

    @GetMapping("/toPenEdit/{tid}")
    public String toPenEdit(@PathVariable("tid") Integer tid, HttpServletRequest request) {
        Business business = businessService.getById(tid);
        request.setAttribute("business", business);
        return "admin/pending/business/bedit";
    }

    @GetMapping("/toCompleted")
    public String toCompleted() {
        return "admin/completed/bcompleted";
    }
    
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult applyBusiness(@RequestBody Business business, HttpSession session) {
        //获得uid
        User user = (User) session.getAttribute("user2");
        business.setUid(user.getUid());
        // 自动设置状态和创建时间
        business.setBstatus("审核中");
        business.setCreated(new Date());
        //执行添加
        boolean flag = businessService.save(business);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }

    @GetMapping("/getList")
    @ResponseBody
    public PageResult<Business> getBusinessList(Integer uid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session
    ) {
        //获得uid
        User user = (User) session.getAttribute("user2");
        uid = user.getUid();
        PageResult<Business> result = businessService.getListByPage(uid, page, limit);
        return result;
    }
    
    //删除（共用）
    @PostMapping("/delete/{bid}")
    @ResponseBody
    public AjaxResult deleteBusiness(@PathVariable("bid") Integer bid) {
        boolean flag = businessService.removeById(bid);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }
    
    //修改(共用)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editBusiness(@RequestBody Business business) {
        boolean flag = businessService.updateById(business);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }

    @GetMapping("/pendingList")
    @ResponseBody
    public PageResult<Business> getPendingList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String realName) {
        System.out.println("-------pendingList------");
        if("".equals(realName)){
            realName = null;
        }
        PageResult<Business> result = businessService.getPendingList(realName, page, limit);
        return result;
    }

    @PostMapping("/approve")
    @ResponseBody
    public AjaxResult approveBusiness(@RequestBody Business business, HttpSession session) {
        try {
            // 获取原始出差记录
            Business originalBusiness = businessService.getById(business.getTid());
            if (originalBusiness == null) {
                return AjaxResult.error("出差记录不存在");
            }
            
            // 只有在"审核中"状态才能审批
            if (!"审核中".equals(originalBusiness.getBstatus())) {
                return AjaxResult.error("该出差申请已被处理");
            }
            
            // 获取当前登录用户
            User user = (User) session.getAttribute("user2");
            if (user == null) {
                return AjaxResult.error("请先登录");
            }
            
            // 更新审批信息
            originalBusiness.setBstatus(business.getBstatus());
            originalBusiness.setApprovedByUserId(user.getUid());
            originalBusiness.setApprovalTime(new Date());
            originalBusiness.setUpdated(new Date());
            
            boolean flag = businessService.updateById(originalBusiness);
            if(flag){
                return AjaxResult.right();
            }else{
                return AjaxResult.error("审批失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("系统错误：" + e.getMessage());
        }
    }

    @GetMapping("/completedList")
    @ResponseBody
    public PageResult<Business> getCompletedList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String realName) {
        if("".equals(realName)){
            realName = null;
        }
        PageResult<Business> result = businessService.getCompletedList(realName, page, limit);
        return result;
    }

    //获取出差详情
    @GetMapping("/getDetail/{tid}")
    @ResponseBody
    public AjaxResult getDetail(@PathVariable("tid") Integer tid) {
        try {
            // 获取出差记录
            Business business = businessService.getById(tid);
            if (business == null) {
                return AjaxResult.error("出差记录不存在");
            }
            
            // 获取申请人信息
            if (business.getUid() != null) {
                User user = userService.getById(business.getUid());
                business.setUser(user);
            }
            
            // 获取审批人信息
            if (business.getApprovedByUserId() != null) {
                User approver = userService.getById(business.getApprovedByUserId());
                business.setApprover(approver);
            }
            
            return AjaxResult.right().put("data", business);
        } catch (Exception e) {
            return AjaxResult.error("系统错误：" + e.getMessage());
        }
    }
}
