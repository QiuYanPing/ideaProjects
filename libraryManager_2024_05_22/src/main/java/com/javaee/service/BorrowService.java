package com.javaee.service;

import com.javaee.pojo.Borrow;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowService {
    List<Borrow> list();

    List<Borrow> selectByUserId(int id);

    void insert(Integer userId,Integer bookId,LocalDateTime returnTime);

    void delete(List<Integer> ids);
}
