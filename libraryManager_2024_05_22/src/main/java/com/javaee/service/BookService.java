package com.javaee.service;

import com.javaee.pojo.Book;
import com.javaee.pojo.PageBean;

import java.util.List;

public interface BookService {

   

    Book selectById(int id);

    void insert(Book book);

    void update(Book book);

    void delete(List<Integer> ids);

    PageBean list(Integer page, Integer pageSize, String name, String author, String category, Float price, String state, Integer borrowNum);

    PageBean orderBy(Integer page, Integer pageSize, String name, String author, String category, Float price, String state, Integer borrowNum);
}
