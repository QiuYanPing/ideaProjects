package com.javaee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private int id;
    private String name;
    private String author;
    private String category;
    private float price;
    private String state;
    private int borrowNum;
}
