package com.javaee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @Autowired
    TestService testService;
    @GetMapping("/get")
    public void getInfo(){
        testService.getInfo();
    }
}
