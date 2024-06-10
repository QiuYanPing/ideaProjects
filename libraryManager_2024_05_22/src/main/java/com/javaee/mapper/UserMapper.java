package com.javaee.mapper;

import com.javaee.pojo.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {


    //@Select("select * from user")
    List<User> list(String userName, String name, Integer gender,
                    LocalDateTime begin, LocalDateTime end);

    @Select("select * from user where id = #{id}")
    User selectById(int id);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user (user_name,password,name,gender,image,site,create_time,update_time)" +
            " values (#{userName},#{password},#{name},#{gender},#{image},#{site},#{createTime},#{updateTime})")
    void insert(User user);

    void update(String userName, String password, String name,Integer gender,
                String image,
                String site,
                String updateTime,Integer id);

    void delete(List<Integer> ids);
    @Select("select * from user where user_name = #{userName} and password = #{password}")
    User selectByUserNamePassword(User user);
}
