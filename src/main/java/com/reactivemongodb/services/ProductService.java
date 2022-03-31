package com.reactivemongodb.services;

import com.reactivemongodb.dtos.ProductDto;
import com.reactivemongodb.repositories.ProductRepository;
import com.reactivemongodb.utils.AppUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Flux<ProductDto> fetchAllProducts() {

        return productRepository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> fetchProductById(String id) {

        return productRepository.findById(id).map(AppUtils::entityToDto)
                // throw exception if product not found.
                .switchIfEmpty(Mono.error(new RuntimeException("Product Not Found")));
    }

    public Flux<ProductDto> fetchProductsInPriceRange(Double min, Double max) {

        return productRepository.findByPriceBetween(Range.closed(min, max)).map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> createNewProduct(Mono<ProductDto> productDtoMono) {

        return productDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(productRepository::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String productId) {

        return productRepository.findById(productId)
                .flatMap(
                        product -> productDtoMono.map(AppUtils::dtoToEntity)
                )
                .doOnNext(product -> product.setId(productId))
                .flatMap(productRepository::save)
                .map(AppUtils::entityToDto)
                .switchIfEmpty(Mono.error(new RuntimeException("Product Not Found")));
    }

    public Mono<Void> deleteProduct(String productId) {

        return productRepository.findById(productId)
                .flatMap(productRepository::delete);
    }
}