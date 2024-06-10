package com.javaee;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestMapper {
    @Select("select * from user")
    void getInfo();
}
