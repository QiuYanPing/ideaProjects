package com.javaee.mapper;

import com.javaee.pojo.Borrow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BorrowMapper {
    @Select("select * from borrow")
    List<Borrow> list();

    @Select("select user_id,book_id,borrow_time,id,name,author,category,price from book,borrow where book_id = id and user_id = #{id}")
    List<Borrow> selectByUserId(int id);

    @Insert("insert into borrow (user_id,book_id,borrow_time,return_time) " +
            "values (#{userId},#{bookId},#{borrowTime},#{returnTime})")
    void insert(Borrow borrow);

    void delete(List<Integer> ids);
}
