package com.javaee.controller;

import com.javaee.pojo.Course;
import com.javaee.pojo.CourseEx;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/course")
public class CourseController {
    @RequestMapping("/to_course_add_basic")
    public String toCourseAddBasic(){
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
    @GetMapping("/to_course_add_pojo")
    public String toCourseAddPojo(){
        return "course/add_course_pojo";
    }
    @PostMapping("/course_add_pojo")
    public String courseAddPojo(Course course, Model model){
        model.addAttribute("course", course);
        return "course/course_info_pojo";
    }
    @GetMapping("/to_course_add_pojo_nest")
    public String toCourseAddNestPojo(){
        return "course/add_course_pojo_nest";
    }
    @PostMapping("/course_add_pojo_nest")
    public String courseAddPojoNest(CourseEx courseEx, Model model){
        model.addAttribute("course", courseEx);
        return "course/course_info_pojo_nest";
    }

}

