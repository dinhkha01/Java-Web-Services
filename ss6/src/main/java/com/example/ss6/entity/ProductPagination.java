package com.example.ss6.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductPagination {
    private List<Product> products;
    private int currentPage;
    private int totalPages;
    private int pageSize ;
}
