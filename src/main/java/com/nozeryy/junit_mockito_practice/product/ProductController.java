package com.nozeryy.junit_mockito_practice.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponse saveProduct(@Valid @RequestBody ProductRequest productRequest){
        return productService.createProduct(productRequest);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id){
        return productService.findProductById(id);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts(){
        return productService.findAllProduct();
    }

    @PutMapping
    public ProductResponse updateProduct(@RequestBody ProductRequest productRequest){
        return productService.updateProduct(productRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
    }

}
