package com.reactivemongodb.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String id;

    private String name;

    private double price;

    private int qty;
}