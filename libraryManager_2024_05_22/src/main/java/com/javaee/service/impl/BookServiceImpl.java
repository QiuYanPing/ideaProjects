package com.javaee.service.impl;

import com.javaee.mapper.BookMapper;
import com.javaee.pojo.Book;
import com.javaee.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookMapper bookMapper;
    @Override
    public List<Book> list() {
        List<Book> bookList = bookMapper.list();
        return bookList;
    }

    @Override
    public Book selectById(int id) {
        Book book = bookMapper.selectById(id);
        return book;
    }

    @Override
    public void insert(Book book) {
        bookMapper.insert(book);
    }

    @Override
    public void update(Book book) {
        bookMapper.update(book);
    }

    @Override
    public void delete(List<Integer> ids) {
        bookMapper.delete(ids);
    }
}
