package com.javaee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {
    private int userId;
    private int bookId;
    private LocalDateTime returnTime;
    private LocalDateTime borrowTime;
    private int id;
    private String name;
    private String author;
    private String category;
    private float price;
}
