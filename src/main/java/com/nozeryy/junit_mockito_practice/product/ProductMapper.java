package com.nozeryy.junit_mockito_practice.product;

import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public Product mapToProduct(ProductRequest productRequest){
        return Product.builder()
                .id(productRequest.id())
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .category(productRequest.category())
                .build();
    }

    public ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .build();
    }

}
