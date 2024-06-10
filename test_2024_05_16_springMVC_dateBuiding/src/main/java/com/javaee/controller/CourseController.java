package com.javaee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/course")
public class CourseController {
    @RequestMapping("/to_course_add_basic")
    public String toCourseAddBasic(){
        System.out.println("get>>>>>>>>>>>>>>>>>>>>");
        return "course/add_course_basic";
    }
    @RequestMapping(value = "/course_add_basic", method = RequestMethod.POST)
    public String courseAddBasic(int cid, String cname, int period,
                                 String bname, String pub, float price, Model model){
        model.addAttribute("cid", cid);
        model.addAttribute("cname", cname);
        model.addAttribute("period", period);
        model.addAttribute("bname", bname);
        model.addAttribute("pub", pub);
        model.addAttribute("price", price);
        return "course/course_info_basic";
    }
}

