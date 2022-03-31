package com.reactivemongodb.utils;

import com.reactivemongodb.dtos.ProductDto;
import com.reactivemongodb.entities.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static ProductDto entityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    public static Product dtoToEntity(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }

    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }
}