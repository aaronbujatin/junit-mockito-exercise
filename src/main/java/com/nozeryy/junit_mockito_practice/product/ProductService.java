package com.nozeryy.junit_mockito_practice.product;

import com.nozeryy.junit_mockito_practice.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest productRequest){
        var product = productRepository.save(productMapper.mapToProduct(productRequest));
        return productMapper.mapToProductResponse(product);
    }

    public ProductResponse findProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product " + id +" was not found"));
        return productMapper.mapToProductResponse(product);
    }

    public List<ProductResponse> findAllProduct(){
        return productRepository.findAll()
                .stream()
                .map(productMapper::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse updateProduct(ProductRequest productRequest){
        Product product = productRepository.findById(productRequest.id())
                .orElseThrow(() -> new ProductNotFoundException("Product " + productRequest.id() +" was not found"));
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setCategory(productRequest.category());
        log.info("Saving the updated product {}", product);
        productRepository.save(product);
        log.info("Updated product from database {}", product);
        return productMapper.mapToProductResponse(product);
    }

    public void deleteProductById(Long id){
        if(!productRepository.existsById(id)){
            throw new ProductNotFoundException("Product id " + id + " was not found!");
        }

        productRepository.deleteById(id);
    }



}
