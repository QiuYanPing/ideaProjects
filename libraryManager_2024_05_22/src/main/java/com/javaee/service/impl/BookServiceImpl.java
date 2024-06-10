package com.javaee.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javaee.mapper.BookMapper;
import com.javaee.pojo.Book;
import com.javaee.pojo.PageBean;
import com.javaee.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookMapper bookMapper;


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

    @Override
    public PageBean list(Integer page, Integer pageSize, String name, String author, String category, Float price, String state, Integer borrowNum) {
        PageHelper.startPage(page,pageSize);

        List<Book> bookList = bookMapper.list(name,author,category,price,state,borrowNum);
        Page<Book> p = (Page<Book>)bookList;
        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());
        return pageBean;
    }
}
