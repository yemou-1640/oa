package com.yemou.oa.controller;

import com.yemou.oa.pojo.Reimbursement;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.ReimbursementService;
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
@RequestMapping("/reimbursement")
public class ReimbursementController {
    @Autowired
    private ReimbursementService reimbursementService;
    @Autowired
    private UserService userService;

    @GetMapping("/toReim")
    public String toReimbursement() {
        System.out.println("toReim");
        return "reimbursement/reim";
    }

    @GetMapping("/toList")
    public String toList() {
        System.out.println("toList:reim");
        return "reimbursement/list";
    }
    
    @GetMapping("/toEdit/{rid}")
    public String toEdit(@PathVariable("rid") Integer rid, HttpServletRequest request) {
        System.out.println("toEdit:reim");
        System.out.println("rid:" + rid);
        Reimbursement reim = reimbursementService.getById(rid);
        request.setAttribute("reim", reim);
        return "reimbursement/edit";
    }

    @GetMapping("/toPending")
    public String toPending() {
        System.out.println("toPending:reim");
        return "admin/pending/reim/rpending";
    }

    @GetMapping("/toApprove/{rid}")
    public String toApprove(@PathVariable("rid") Integer rid, HttpServletRequest request) {
        System.out.println("toApprove:reim");
        Reimbursement reimbursement = reimbursementService.getById(rid);
        if (reimbursement != null && reimbursement.getUid() != null) {
            User user = userService.getById(reimbursement.getUid());
            reimbursement.setUser(user);
        }
        request.setAttribute("reimbursement", reimbursement);
        return "admin/pending/reim/rApprove";
    }

    @GetMapping("/toPenEdit/{rid}")
    public String toPenEdit(@PathVariable("rid") Integer rid, HttpServletRequest request) {
        System.out.println("toPenEdit:reim");
        Reimbursement reimbursement = reimbursementService.getById(rid);
        request.setAttribute("reimbursement", reimbursement);
        return "admin/pending/reim/redit";
    }

    @GetMapping("/toCompleted")
    public String toCompleted() {
        System.out.println("toCompleted:reim");
        return "admin/completed/rcompleted";
    }

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult applyReimbursement(@RequestBody Reimbursement reimbursement, HttpSession session) {
        System.out.println("add:reim");
        User user = (User) session.getAttribute("user2");
        reimbursement.setUid(user.getUid());
        reimbursement.setRstatus("审核中");
        reimbursement.setCreated(new Date());
        boolean flag = reimbursementService.save(reimbursement);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }

    @GetMapping("/getList")
    @ResponseBody
    public PageResult<Reimbursement> getReimbursementList(Integer uid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session
    ) {
        System.out.println("getList:reim");
        User user = (User) session.getAttribute("user2");
        uid = user.getUid();
        PageResult<Reimbursement> result = reimbursementService.getListByPage(uid, page, limit);
        return result;
    }
    
    @PostMapping("/cancel/{rid}")
    public AjaxResult cancelReimbursement(@PathVariable Integer rid) {
        Reimbursement reimbursement = reimbursementService.getById(rid);
        if(reimbursement != null && "审核中".equals(reimbursement.getRstatus())) {
            reimbursementService.removeById(rid);
            return AjaxResult.right();
        }
        return AjaxResult.error();
    }
    
    @PostMapping("/delete/{rid}")
    @ResponseBody
    public AjaxResult deleteReimbursement(@PathVariable("rid") Integer rid) {
        boolean flag = reimbursementService.removeById(rid);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }
    
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editReimbursement(@RequestBody Reimbursement reimbursement) {
        System.out.println("edit:reim");
        boolean flag = reimbursementService.updateById(reimbursement);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }

    @GetMapping("/pendingList")
    @ResponseBody
    public PageResult<Reimbursement> getPendingList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String realName) {
        if("".equals(realName)){
            realName = null;
        }
        PageResult<Reimbursement> result = reimbursementService.getPendingList(realName, page, limit);
        return result;
    }

    @PostMapping("/approve")
    @ResponseBody
    public AjaxResult approveReimbursement(@RequestBody Reimbursement reimbursement, HttpSession session) {
        try {
            Reimbursement originalReimbursement = reimbursementService.getById(reimbursement.getRid());
            if (originalReimbursement == null) {
                return AjaxResult.error("报销记录不存在");
            }
            
            if (!"审核中".equals(originalReimbursement.getRstatus())) {
                return AjaxResult.error("该报销申请已被处理");
            }
            
            User user = (User) session.getAttribute("user2");
            if (user == null) {
                return AjaxResult.error("请先登录");
            }
            
            originalReimbursement.setRstatus(reimbursement.getRstatus());
            originalReimbursement.setApprovedByUserId(user.getUid());
            originalReimbursement.setApprovalTime(new Date());
            originalReimbursement.setUpdated(new Date());
            
            boolean flag = reimbursementService.updateById(originalReimbursement);
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
    public PageResult<Reimbursement> getCompletedList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String realName) {
        if("".equals(realName)){
            realName = null;
        }
        PageResult<Reimbursement> result = reimbursementService.getCompletedList(realName, page, limit);
        return result;
    }

    @GetMapping("/getDetail/{rid}")
    @ResponseBody
    public AjaxResult getDetail(@PathVariable("rid") Integer rid) {
        try {
            Reimbursement reimbursement = reimbursementService.getById(rid);
            if (reimbursement == null) {
                return AjaxResult.error("报销记录不存在");
            }
            
            if (reimbursement.getUid() != null) {
                User user = userService.getById(reimbursement.getUid());
                reimbursement.setUser(user);
            }
            
            if (reimbursement.getApprovedByUserId() != null) {
                User approver = userService.getById(reimbursement.getApprovedByUserId());
                reimbursement.setApprover(approver);
            }
            
            return AjaxResult.right().put("data", reimbursement);
        } catch (Exception e) {
            return AjaxResult.error("系统错误：" + e.getMessage());
        }
    }
}
