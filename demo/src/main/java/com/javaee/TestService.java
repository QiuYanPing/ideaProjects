package com.javaee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    TestMapper testMapper;
    public void getInfo() {
        testMapper.getInfo();
    }
}
