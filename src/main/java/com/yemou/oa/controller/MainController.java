package com.yemou.oa.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    //返回欢迎页面
    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    //    跳转到首页
    @RequestMapping("/toIndex")
    public String toIndex() {
        System.out.println("-----toIndex-----");
        return "content";

    }
    
    //    跳转到管理员首页
    @RequestMapping("/toAdmin")
    public String toAdmin() {
        System.out.println("-----toAdmin-----");
        return "admin/content";
    }

}

