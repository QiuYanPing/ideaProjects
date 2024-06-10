package com.javaee.controller;

import com.javaee.pojo.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class BookController {
    @GetMapping("/to_book_list")
    public String toBookList(){
        return "book/book_list";
    }
    @PostMapping("/add_book")
    @ResponseBody
    public String addStudent(@RequestBody Book book){
        System.out.println(book);
        return "OK";
    }
}
