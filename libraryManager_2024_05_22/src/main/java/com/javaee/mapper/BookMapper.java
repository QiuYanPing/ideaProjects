package com.javaee.mapper;

import com.javaee.pojo.Book;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("select * from book")
    List<Book> list();

    @Select("select * from book where id = #{id}")
    Book selectById(int id);
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into book (name,author,category,price,borrow_num) " +
            "values (#{name},#{author},#{category},#{price},#{borrowNum})")
    void insert(Book book);

    void update(Book book);

    void delete(List<Integer> ids);
}
