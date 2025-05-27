package com.yemou.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yemou.oa.pojo.Attendance;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.AttendanceService;
import com.yemou.oa.service.UserService;
import com.yemou.oa.utils.AjaxResult;
import com.yemou.oa.utils.PageResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping("/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private UserService userService;

    // 跳转到考勤打卡页面
    @GetMapping("/toAttendance")
    public String toAtten() {
        System.out.println("-----toAttendance-----");
        return "attendance/atten";
    }
    
    // 跳转到考勤记录页面--管理员
    @GetMapping("/toReport")
    public String toList() {
        System.out.println("-----toList-----");
        return "admin/attendance/list";
    }
    
    // 跳转到编辑页面--管理员
    @GetMapping("/toEdit/{aid}")
    public String toDetail(@PathVariable("aid") Integer aid, Model model) {
        System.out.println("-----attendance：toEdit-----");
        Attendance attendance = attendanceService.getById(aid);
        model.addAttribute("attendance", attendance);
        return "admin/attendance/edit";
    }

    // 打卡（签到或签退）
    @PostMapping("/clock")
    @ResponseBody
    public AjaxResult clock(@RequestBody Attendance attendance, HttpSession session) {
        System.out.println("-----clock-----");
        System.out.println("attendance:" + attendance);
        
        // 获取当前用户
        User user = (User) session.getAttribute("user2");
        
        Integer uid = user.getUid();
        attendance.setUid(uid);
        
        // 设置当前时间
        Date now = new Date();
        attendance.setTime(now);
        attendance.setDate(now);
        
        // 检查今天是否已经有相同类型的打卡记录
        if (attendanceService.hasClockToday(uid, attendance.getType())) {
            return AjaxResult.error("今天已经" + ("签到".equals(attendance.getType()) ? "签到" : "签退") + "过了");
        }
        
        boolean flag = attendanceService.save(attendance);
        if(flag) {
            return AjaxResult.right();
        } else {
            return AjaxResult.error("打卡失败");
        }
    }
    
    // 获取今日打卡记录
    @GetMapping("/records")
    @ResponseBody
    public AjaxResult records(HttpSession session) {
        System.out.println("-----records-----");
        User user = (User) session.getAttribute("user2");
        if (user == null) {
            return AjaxResult.error("请先登录");
        }
        
        Integer uid = user.getUid();
        System.out.println("----todayRecords----");
        List<Attendance> records = attendanceService.getTodayRecords(uid);
        return AjaxResult.right().put("data", records);
    }
    
    //管理员端-获取考勤列表
    @GetMapping("/getList")
    @ResponseBody
    public PageResult<Attendance> getAttendanceList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String realName) {
        System.out.println("-----getList:Attendance-----");
        if("".equals(realName)){
            realName = null;
        }
        PageResult<Attendance> result = attendanceService.getListByPage(realName, page, limit);
        return result;
    }

    //删除考勤记录（共用）
    @PostMapping("/delete/{aid}")
    @ResponseBody
    public AjaxResult deleteAttendance(@PathVariable("aid") Integer aid) {
        System.out.println("-----delete:Attendance-----");
        System.out.println("aid:" + aid);
        boolean flag = attendanceService.removeById(aid);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }

    //管理员端-修改考勤记录
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editAttendance(@RequestBody Attendance attendance) {
        System.out.println("--------edit:Attendance-------");
        System.out.println("attendance:" + attendance);
        boolean flag = attendanceService.updateById(attendance);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }

}
