package com.javaee.service;

import com.javaee.pojo.Book;

import java.util.List;

public interface BookService {

    List<Book> list();

    Book selectById(int id);

    void insert(Book book);

    void update(Book book);

    void delete(List<Integer> ids);
}
