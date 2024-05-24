package com.javaee.service.impl;

import com.javaee.mapper.BorrowMapper;
import com.javaee.pojo.Borrow;
import com.javaee.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    BorrowMapper borrowMapper;
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

    @Override
    public void insert(Integer userId,Integer bookId,LocalDateTime returnTime) {
        Borrow borrow = new Borrow();
        borrow.setBorrowTime(LocalDateTime.now());
        borrow.setUserId(userId);
        borrow.setBookId(bookId);
        borrow.setReturnTime(returnTime);
        borrowMapper.insert(borrow);
    }

    @Override
    public void delete(List<Integer> ids) {
        borrowMapper.delete(ids);
    }
}
