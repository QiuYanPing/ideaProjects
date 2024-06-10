package com.javaee.controller;

import com.javaee.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {
    @GetMapping("/to_add_student")
    public String toAddStudent(){
        return "student/add_student";
    }
    @PostMapping("/add_student")
    public String addStudent(Student student, Model model){
        model.addAttribute("student",student);
        return "student/student_info";
    }
}

