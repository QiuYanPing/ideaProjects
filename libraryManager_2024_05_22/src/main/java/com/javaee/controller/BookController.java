package com.javaee.controller;

import com.javaee.anno.Log;
import com.javaee.pojo.Book;
import com.javaee.pojo.Result;
import com.javaee.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/book")
    public Result list(){
        List<Book> bookList = bookService.list();
        log.info("查询所用图书信息");
        return Result.success(bookList);
    }
    @GetMapping("/book/{id}")
    public Result selectById(@PathVariable int id){
        Book book = bookService.selectById(id);
        log.info("根据id查询图书：{}",id);
        return Result.success(book);
    }
    @Log
    @PostMapping("/book")
    public Result insert(@RequestBody Book book){
        bookService.insert(book);
        log.info("添加图书id:{}",book.getId());
        return Result.success();
    }
    @Log
    @PutMapping("/book")
    public Result update(@RequestBody Book book){
        bookService.update(book);
        log.info("修改图书id:{}",book.getId());
        return Result.success();
    }
    @Log
    @DeleteMapping("/book/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        bookService.delete(ids);
        log.info("删除图书id:{}",ids);
        return Result.success();
    }
}
