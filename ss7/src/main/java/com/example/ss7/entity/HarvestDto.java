package com.example.ss7.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HarvestDto {
    private Long id;
    private String name;
    private int quantity;
    private Double totalPrice;
}
