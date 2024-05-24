package com.javaee.controller;

import com.javaee.anno.Log;
import com.javaee.pojo.Borrow;
import com.javaee.pojo.Result;
import com.javaee.service.BookService;
import com.javaee.service.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@RestController
public class BorrowController {
    @Autowired
    BorrowService borrowService;
    @GetMapping("/borrow")
    public Result list(){
        List<Borrow> borrowList = borrowService.list();
        log.info("查询借书历史");
        return Result.success(borrowList);
    }
    @GetMapping("/borrow/{id}")
    public Result selectByUserId(@PathVariable int id){
        List<Borrow> borrowList = borrowService.selectByUserId(id);
        log.info("根据用户id查询借书历史：{}",id);
        return Result.success(borrowList);
    }
    @Log
    @PostMapping("/borrow")
    public Result insert(Integer userId,Integer bookId,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime returnTime){
        borrowService.insert(userId,bookId,returnTime);
        log.info("添加借书历史，用户id:{},图书id:{}",userId,bookId);
        return Result.success();
    }
    @Log
    @DeleteMapping("/borrow/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        borrowService.delete(ids);
        log.info("删除借书历史ids:{}",ids);
        return Result.success();
    }
}
