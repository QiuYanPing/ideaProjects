package com.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.po.Student;
import com.service.StudentService;

@Controller
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping("/studentlist")
    public String studentList(Model model) {
        List<Student> list = studentService.findAllStudent();
        model.addAttribute("studentList", list);
        return "student/studentlist";
    }

    @RequestMapping(value="/studentpreinsert",method=RequestMethod.GET)
    public String studentPreinsert() {
        return "student/studentadd";
    }

    @RequestMapping(value="/studentinsert", method=RequestMethod.POST)
    public String studentInsert(Student student){
        studentService.addStudent(student);
        return "redirect:studentlist";
    }

    @RequestMapping(value="/studentdelete", method=RequestMethod.POST)
    public String studentDelete(String[] snoArray) {
        studentService.deleteStudents(snoArray);
        return "redirect:studentlist";
    }

    @RequestMapping(value="/studentpreupdate", method=RequestMethod.GET)
    public String studentPreupdate(Student student, Model model) {
        model.addAttribute("student", student);
        return "student/studentupdate";
    }

    @RequestMapping(value="/studentupdate", method=RequestMethod.POST)
    public String studentUpdate(Student student) {
        studentService.updateStudent(student);
        return "redirect:studentlist";
    }
}
