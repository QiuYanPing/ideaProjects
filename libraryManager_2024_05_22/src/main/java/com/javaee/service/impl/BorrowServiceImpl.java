package com.javaee.service.impl;

import com.javaee.mapper.BookMapper;
import com.javaee.mapper.BorrowMapper;
import com.javaee.pojo.Book;
import com.javaee.pojo.Borrow;
import com.javaee.service.BookService;
import com.javaee.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    BorrowMapper borrowMapper;
    @Autowired
    BookMapper bookMapper;
    @Override
    public List<Borrow> list() {
        List<Borrow> borrowList = borrowMapper.list();
        return borrowList;
    }

    @Override
    public List<Borrow> selectByUserId(int id) {
        List<Borrow> borrowList = borrowMapper.selectByUserId(id);
        return borrowList;
    }
    @Transactional
    @Override
    public void insert(Integer userId,Integer bookId,LocalDateTime returnTime) {
        Book book = bookMapper.selectById(userId);
        book.setState("已借");
        book.setBorrowNum(book.getBorrowNum()+1);
        bookMapper.update(book);


        Borrow borrow = new Borrow();
        borrow.setBorrowTime(LocalDateTime.now());
        borrow.setUserId(userId);
        borrow.setBookId(bookId);
        borrow.setReturnTime(returnTime);
        borrowMapper.insert(borrow);
    }
    @Transactional
    @Override
    public void delete(List<Integer> ids) {
        for (Integer id : ids) {
            Book book = bookMapper.selectById(id);
            book.setState("在架");
            bookMapper.update(book);
        }
        borrowMapper.delete(ids);
    }
}
