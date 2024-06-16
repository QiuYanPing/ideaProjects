package com.javaee.controller;

import com.javaee.anno.Log;
import com.javaee.pojo.Book;
import com.javaee.pojo.PageBean;
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
    @GetMapping("/book/orderBy")
    public Result orderBy(@RequestParam(defaultValue = "1")Integer page,
                          @RequestParam(defaultValue = "10")Integer pageSize,
                          String name,String author,String category,
                          Float price,String state,Integer borrowNum){
        log.info("查询借出图书的前十：{}",category);
        PageBean pageBean = bookService.orderBy(page,pageSize,name,author,category,price,state,borrowNum);
        return Result.success(pageBean);
    }

    @GetMapping("/book")
    public Result list(@RequestParam(defaultValue = "1")Integer page,
                       @RequestParam(defaultValue = "10")Integer pageSize,
                       String name,String author,String category,
                       Float price,String state,Integer borrowNum){
        log.info("分页查询，参数：{},{}",page,pageSize);
        PageBean pageBean = bookService.list(page,pageSize,name,author,category,price,state,borrowNum);
        return Result.success(pageBean);
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
