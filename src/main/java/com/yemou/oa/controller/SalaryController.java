package com.yemou.oa.controller;

import com.yemou.oa.pojo.Salary;
import com.yemou.oa.pojo.User;
import com.yemou.oa.service.SalaryService;
import com.yemou.oa.service.UserService;
import com.yemou.oa.utils.AjaxResult;
import com.yemou.oa.utils.PageResult;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/salary")
public class SalaryController {
    @Autowired
    private SalaryService salaryService;
    @Autowired
    private UserService userService;

    //跳转到工资管理页面
    @GetMapping("/toReport")
    public String toList() {
        System.out.println("------toReport------");
        return "admin/salary/list";
    }
    
    //跳转到添加工资记录页面
    @GetMapping("/toAdd")
    public String toAdd() {
        System.out.println("------toAdd------");
        return "admin/salary/add";
    }
    
    //跳转到编辑工资记录页面
    @GetMapping("/toEdit/{sid}")
    public String toEdit(@PathVariable("sid") Integer sid, HttpServletRequest request) {
        System.out.println("------toEdit------");
        Salary salary = salaryService.getById(sid);
        if (salary != null && salary.getUid() != null) {
            User user = userService.getById(salary.getUid());
            salary.setUser(user);
        }
        request.setAttribute("salary", salary);
        return "admin/salary/edit";
    }
    
    //获取工资列表
    @GetMapping("/getList")
    @ResponseBody
    public PageResult<Salary> getSalaryList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String realName) {
        System.out.println("------salary:getList------");
        System.out.println("page:" + page);
        System.out.println("limit:" + limit);
        System.out.println("realName:" + realName);
        if("".equals(realName)){
            realName = null;
        }
        PageResult<Salary> result = salaryService.getListByPage(realName, page, limit);
        return result;
    }

    //删除工资记录
    @PostMapping("/delete/{sid}")
    @ResponseBody
    public AjaxResult deleteSalary(@PathVariable("sid") Integer sid) {
        System.out.println("------delete:Salary------");
        boolean flag = salaryService.removeById(sid);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }

    //修改工资记录
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSalary(@RequestBody Salary salary) {
        System.out.println("------salary:edit------");
        salary.setUpdated(new Date());
        boolean flag = salaryService.updateById(salary);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }
    
    //添加工资记录
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSalary(@RequestBody Salary salary) {
        System.out.println("------salary:add------");
        salary.setCreated(new Date());
        salary.setUpdated(new Date());
        boolean flag = salaryService.save(salary);
        if(flag){
            return AjaxResult.right();
        }else{
            return AjaxResult.error();
        }
    }
}
