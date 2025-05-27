package com.yemou.oa.controller;

import com.yemou.oa.pojo.Leave;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.LeaveService;
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
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private UserService userService;

    //跳转请假申请页面
    @GetMapping("/toLeave")
    public String toLeave() {
        System.out.println("----toLeave----");
        return "leave/applyLeave";
    }
    //跳转申请记录页面
    @GetMapping("/toList")
    public String toList() {
        System.out.println("----toList----");
        return "leave/list";
    }
    @GetMapping("/toEdit/{lid}")
    public String toEdit(@PathVariable("lid") Integer lid, HttpServletRequest request) {
        System.out.println("----toEdit----");
        System.out.println("lid:" + lid);
        Leave leave = leaveService.getById(lid);
        request.setAttribute("leave", leave);
        return "leave/edit";
    }
    @GetMapping("/toPending")
    public String toPending() {
        System.out.println("----leave:toPending----");
        return "admin/pending/leave/lpending";
    }
    //跳转到审批页面
    @GetMapping("/toApprove/{lid}")
    public String toApprove(@PathVariable("lid") Integer lid, HttpServletRequest request) {
        System.out.println("----toApprove:leave----");
        System.out.println("lid:" + lid);
        // 获取请假记录并加载用户信息
        Leave leave = leaveService.getById(lid);
        if (leave != null && leave.getUid() != null) {
            // 加载用户信息
            User user = userService.getById(leave.getUid());
            leave.setUser(user);
        }
        request.setAttribute("leave", leave);
        return "admin/pending/leave/lApprove";
    }
    //审批编辑
    @GetMapping("/toPenEdit/{lid}")
    public String toPenEdit(@PathVariable("lid") Integer lid, HttpServletRequest request) {
        System.out.println("----leave:toPenEdit----");
        System.out.println("lid:" + lid);
        Leave leave = leaveService.getById(lid);
        request.setAttribute("leave", leave);
        return "admin/pending/leave/ledit";
    }
    //跳转已办审批
    @GetMapping("/toCompleted")
    public String toCompleted() {
        System.out.println("----leave:toCompleted----");
        return "admin/completed/lcompleted";
    }

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult applyLeave(@RequestBody Leave leave,HttpSession session) {
        System.out.println("----add----");
        System.out.println("leave:"+leave);
        //获得uid
        User user = (User) session.getAttribute("user2");
        leave.setUid(user.getUid());
        // 自动设置状态和创建时间
        leave.setLstatus("审核中");
        leave.setCreated(new Date());
        //执行添加
        boolean flag=leaveService.save(leave);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }

    }

    @GetMapping("/getList")
    @ResponseBody
    public PageResult<Leave> getLeaveList(Integer uid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session
    ) {
        System.out.println("----getList----");
        //获得uid
        User user = (User) session.getAttribute("user2");
        uid=user.getUid();
        System.out.println("uid:"+uid);
        System.out.println("page:"+page);
        System.out.println("limit:"+limit);
        PageResult<Leave> result =leaveService.getListByPage(uid,page,limit);
        return result;
    }

    @PostMapping("/cancel/{lid}")
    public AjaxResult cancelLeave(@PathVariable Integer lid) {
        Leave leave = leaveService.getById(lid);
        if(leave != null && "审核中".equals(leave.getLstatus())) {
            leaveService.removeById(lid);
            return AjaxResult.right();
        }
        return AjaxResult.error();
    }

    //删除（共用）
    @PostMapping("/delete/{lid}")
    @ResponseBody
    public AjaxResult deleteLeave(@PathVariable("lid") Integer lid) {
        System.out.println("----delete:Leave----");
        System.out.println("lid:"+lid);
        boolean flag=leaveService.removeById(lid);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }

    //修改(共用)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editLeave(@RequestBody Leave leave) {
        System.out.println("----edit:Leave----");
        System.out.println("leave:"+leave);
        boolean flag=leaveService.updateById(leave);
        if(flag){
        return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }
    //管理员端-待审批列表
    @GetMapping("/pendingList")
    @ResponseBody
    public PageResult<Leave> getPendingList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String realName) {
        System.out.println("-------leave:getPendingList---------");
        System.out.println("realName:"+realName);
        System.out.println("page:"+page);
        System.out.println("limit:"+limit);
        if("".equals(realName)){
            realName=null;
        }
        PageResult<Leave> result =leaveService.getPendingList(realName,page,limit);

        return result;
    }
    //管理员端-审批
    @PostMapping("/approve")
    @ResponseBody
    public AjaxResult approveLeave(@RequestBody Leave leave, HttpSession session) {
        System.out.println("----leave:approve----");
        System.out.println("leave:" + leave);
        
        try {
            // 获取原始请假记录
            Leave originalLeave = leaveService.getById(leave.getLid());
            if (originalLeave == null) {
                return AjaxResult.error("请假记录不存在");
            }
            
            // 只有在"审核中"状态才能审批
            if (!"审核中".equals(originalLeave.getLstatus())) {
                return AjaxResult.error("该请假申请已被处理");
            }
            
            // 获取当前登录用户
            User user = (User) session.getAttribute("user2");
            if (user == null) {
                return AjaxResult.error("请先登录");
            }
            
            // 更新审批信息
            originalLeave.setLstatus(leave.getLstatus());
            originalLeave.setApprovedByUserId(user.getUid());
            originalLeave.setApprovalTime(new Date());
            originalLeave.setUpdated(new Date());
            
            boolean flag = leaveService.updateById(originalLeave);
            if(flag){
                return AjaxResult.right();
            }else{
                return AjaxResult.error("审批失败");
            }
        } catch (Exception e) {
            System.err.println("审批请假申请时发生错误：" + e.getMessage());
            return AjaxResult.error("系统错误：" + e.getMessage());
        }
    }

    //已办审批列表
    @GetMapping("/completedList")
    @ResponseBody
    public PageResult<Leave> getCompletedList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String realName) {
        System.out.println("-------leave:getCompletedList---------");
        System.out.println("realName:"+realName);
        System.out.println("page:"+page);
        System.out.println("limit:"+limit);
        if("".equals(realName)){
            realName=null;
        }
        PageResult<Leave> result = leaveService.getCompletedList(realName, page, limit);
        return result;
    }

    //获取请假详情
    @GetMapping("/getDetail/{lid}")
    @ResponseBody
    public AjaxResult getDetail(@PathVariable("lid") Integer lid) {
        System.out.println("----getDetail:leave----");
        System.out.println("lid:" + lid);
        
        try {
            // 获取请假记录
            Leave leave = leaveService.getById(lid);
            if (leave == null) {
                return AjaxResult.error("请假记录不存在");
            }
            
            // 获取申请人信息
            if (leave.getUid() != null) {
                User user = userService.getById(leave.getUid());
                leave.setUser(user);
            }
            
            // 获取审批人信息
            if (leave.getApprovedByUserId() != null) {
                User approver = userService.getById(leave.getApprovedByUserId());
                leave.setApprover(approver);
            }
            
            return AjaxResult.right().put("data", leave);
        } catch (Exception e) {
            System.err.println("获取请假详情时发生错误：" + e.getMessage());
            return AjaxResult.error("系统错误：" + e.getMessage());
        }
    }

}
