package com.reactivemongodb.controllers;

import com.reactivemongodb.dtos.ProductDto;
import com.reactivemongodb.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @RequestMapping("")
    public Flux<ProductDto> fetchAllProducts() {

        return productService.fetchAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> fetchProductById(@PathVariable String id) {

        return productService.fetchProductById(id);
    }

    @GetMapping("/price-range")
    public Flux<ProductDto> fetchProductsInPriceRange(@RequestParam("min") double min,
                                                      @RequestParam("max") double max) {

        return productService.fetchProductsInPriceRange(min, max);
    }

    @PostMapping("")
    public Mono<ProductDto> createNewProduct(@RequestBody Mono<ProductDto> productRequest) {

        return productService.createNewProduct(productRequest);
    }

    @PutMapping("/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productRequest,
                                          @PathVariable String id) {

        return productService.updateProduct(productRequest, id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {

        return productService.deleteProduct(id);
    }
}