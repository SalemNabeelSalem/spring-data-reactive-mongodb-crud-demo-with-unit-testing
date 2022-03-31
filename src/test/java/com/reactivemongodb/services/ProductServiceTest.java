package com.reactivemongodb.services;

import com.reactivemongodb.controllers.ProductController;
import com.reactivemongodb.dtos.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class ProductServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("Fetch All Products Test")
    void FetchAllProductsTest() {

        Flux<ProductDto> productDtoFlux = Flux.just(
                new ProductDto("101", "Product 101", 1100, 11),
                new ProductDto("102", "Product 102", 1200, 12),
                new ProductDto("103", "Product 103", 1300, 13)
        );

        when(productService.fetchAllProducts()).thenReturn(productDtoFlux);

        String uri = "/api/v1/products";

        Flux<ProductDto> response = webTestClient.get().uri(uri).exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(response)
                .expectSubscription()
                .expectNext(new ProductDto("101", "Product 101", 1100, 11))
                .expectNext(new ProductDto("102", "Product 102", 1200, 12))
                .expectNext(new ProductDto("103", "Product 103", 1300, 13))
                .verifyComplete();
    }

    @Test
    @DisplayName("Fetch Product By Id Test")
    void FetchProductByIdTest() {

        Mono<ProductDto> productDtoMono = Mono.just(
                new ProductDto("101", "Product 101", 1100, 11)
        );

        when(productService.fetchProductById("101")).thenReturn(productDtoMono);

        String uri = "/api/v1/products/101";

        Flux<ProductDto> response = webTestClient.get().uri(uri).exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(response)
                .expectSubscription()
                .expectNextMatches(ProductDto -> ProductDto.getName().equals("Product 101"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Fetch Products In Price Range Test")
    void FetchProductsInPriceRangeTest() {

        Flux<ProductDto> productDtoFlux = Flux.just(
                new ProductDto("101", "Product 101", 1800, 11),
                new ProductDto("102", "Product 102", 1200, 12),
                new ProductDto("103", "Product 103", 1700, 13)
        );

        when(productService.fetchProductsInPriceRange(1000.0, 1800.0)).thenReturn(productDtoFlux);

        String uri = "/api/v1/products/price-range?min=1000&max=1800";

        Flux<ProductDto> response = webTestClient.get().uri(uri).exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(response)
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    @DisplayName("Create New Product Test")
    void CreateNewProductTest() {

        Mono<ProductDto> productDtoMono = Mono.just(
                new ProductDto("101", "Product 101", 1100, 11)
        );

        when(productService.createNewProduct(productDtoMono)).thenReturn(productDtoMono);

        String uri = "/api/v1/products";

        webTestClient.post().uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();
    }

    @Test
    @DisplayName("Update Product Test")
    void UpdateProductTest() {

        Mono<ProductDto> productDtoMono = Mono.just(
                new ProductDto("101", "Product 101", 1100, 11)
        );

        when(productService.updateProduct(productDtoMono, "101")).thenReturn(productDtoMono);

        String uri = "/api/v1/products/101";

        webTestClient.put().uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();
    }

    @Test
    @DisplayName("Delete Product Test")
    void DeleteProductTest() {

        given(productService.deleteProduct(any())).willReturn(Mono.empty());

        String uri = "/api/v1/products/101";

        webTestClient.delete().uri(uri).exchange().expectStatus().isOk();
    }
}