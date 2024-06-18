package com.javaee.controller;

import com.javaee.anno.Log;
import com.javaee.pojo.Book;
import com.javaee.pojo.Borrow;
import com.javaee.pojo.Result;
import com.javaee.service.BookService;
import com.javaee.service.BorrowService;
import com.javaee.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@RestController
public class BorrowController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    BorrowService borrowService;
    @Autowired
    BookService bookService;
    /*@GetMapping("/borrow")
    public Result list(){
        List<Borrow> borrowList = borrowService.list();
        log.info("查询借书历史");
        return Result.success(borrowList);
    }*/

    @GetMapping("/borrow")
    public Result selectByUserId(){
        String jwt = request.getHeader("token");
        /*request.getHeader("token")*/
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer id = (Integer) claims.get("id");

        List<Borrow> borrowList = borrowService.selectByUserId(id);
        log.info("根据用户id查询借书历史：{}",id);
        return Result.success(borrowList);
    }
    @Log
    @PostMapping("/borrow")
    public Result insert(Integer bookId,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime returnTime){
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer userId = (Integer) claims.get("id");
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
