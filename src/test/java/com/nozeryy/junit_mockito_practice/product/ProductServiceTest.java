package com.nozeryy.junit_mockito_practice.product;

import com.nozeryy.junit_mockito_practice.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    private ProductService productService;
    private Product productToDb;
    private Product productFromDb;
    private ProductResponse expectedProductResponse;
    private ProductRequest productRequest;
    private ProductRequest productUpdate;

    @BeforeEach
    void setUp(){
        productService = new ProductService(productRepository, productMapper);
        productToDb = new Product(null, "RTX 2060", "Product description",valueOf(20_000), "Graphics Card");
        productFromDb = new Product(1L, "RTX 2060", "Product description",valueOf(20_000), "Graphics Card");
        productRequest = new ProductRequest(null, "RTX 2060", "Product description",valueOf(20_000), "Graphics Card");
        productUpdate = new ProductRequest(1L, "RTX 2060", "Product description",valueOf(20_000), "Graphics Card");
        expectedProductResponse = new ProductResponse(1L, "RTX 2060", "Product description",valueOf(20_000), "Graphics Card");
    }

    @Test
    public void testCreateProduct(){
        //arrange
        given(productMapper.mapToProduct(productRequest)).willReturn(productToDb);
        given(productRepository.save(productToDb)).willReturn(productFromDb);
        given(productMapper.mapToProductResponse(productFromDb)).willReturn(expectedProductResponse);

        //act
        ProductResponse actualProductResponse = productService.createProduct(productRequest);

        //assert
        assertNotNull(actualProductResponse);
        assertEquals(expectedProductResponse, actualProductResponse);
        verify(productMapper, times(1)).mapToProduct(productRequest);
        verify(productRepository, times(1)).save(productToDb);
        verify(productMapper, times(1)).mapToProductResponse(productFromDb);
    }

    @Test
    public void testGetProductById(){
        //arrange
        Long id = 1L;
        given(productRepository.findById(id)).willReturn(Optional.of(productFromDb));
        given(productMapper.mapToProductResponse(productFromDb)).willReturn(expectedProductResponse);

        //act
        ProductResponse actualProductResponse = productService.findProductById(id);

        //assert
        assertNotNull(actualProductResponse);
        assertThat(expectedProductResponse.name()).isEqualTo(actualProductResponse.name());
        assertEquals(expectedProductResponse.id(), actualProductResponse.id());
        verify(productRepository,times(1)).findById(id);
    }

    @Test
    public void testShouldThrowWhenGettingNonExistentProduct(){
        Long nonExistingId = 999L;
        given(productRepository.findById(nonExistingId)).willReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(nonExistingId));
//        assertThatThrownBy(() -> productService.updateProduct(productRequest))
//                .isInstanceOf(ProductNotFoundException.class)
//                .hasMessageContaining("Product " + productRequest.id() +" was not found");

        verify(productRepository, times(1)).findById(nonExistingId);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productMapper);
    }

    @Test
    void testGetAllProductsAndReturnAsProductResponse(){
        List<Product> savedProductsFromDB = List.of(
                new Product(1L, "PN1", "PD1", valueOf(2_100), "PC1"),
                new Product(2L, "PN2", "PD2", valueOf(2_100), "PC2"),
                new Product(3L, "PN3", "PD3", valueOf(2_100), "PC3")
        );

        List<ProductResponse> expectedProductResponse = List.of(
                new ProductResponse(1L, "PN1", "PD1", valueOf(2_100), "PC1"),
                new ProductResponse(2L, "PN2", "PD2", valueOf(2_100), "PC2"),
                new ProductResponse(3L, "PN3", "PD3", valueOf(2_100), "PC3")
        );

        given(productRepository.findAll()).willReturn(savedProductsFromDB);
        for(int i=0; i<savedProductsFromDB.size(); i++){
            given(productMapper.mapToProductResponse(savedProductsFromDB.get(i)))
                    .willReturn(expectedProductResponse.get(i));
        }

        List<ProductResponse> actualProductResponses = productService.findAllProduct();

        assertEquals(expectedProductResponse, actualProductResponses);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateProduct(){
        Long id = 1L;
        given(productRepository.findById(id)).willReturn(Optional.of(productFromDb));
        given(productRepository.save(productRequestToUpdate).willReturn(productFromDatabase);
        given(productMapper.mapToProductResponse(productFromDatabase)).willReturn(expectedProductResponse);

        ProductResponse actualProductResponse = productService.updateProduct(productRequest);

        assertEquals(expectedProductResponse, actualProductResponse);
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(any(Product.class));

    }

    @Test
    public void testShouldThrowWhenUpdatingNonExistentProduct(){
        Long nonExistingId = 999L;
        ProductRequest productRequest = new ProductRequest(nonExistingId,"PN1", "PD1", valueOf(20), "PC1");
        given(productRepository.findById(nonExistingId)).willReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productRequest));
//        assertThatThrownBy(() -> productService.updateProduct(productRequest))
//                .isInstanceOf(ProductNotFoundException.class)
//                .hasMessageContaining("Product " + productRequest.id() +" was not found");

        verify(productRepository, times(1)).findById(nonExistingId);
        verifyNoInteractions(productMapper);
    }

    @Test
    public void testDeleteProductById(){
        Long id = 1L;
        given(productRepository.existsById(id)).willReturn(true);

        productService.deleteProductById(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    public void testShouldThrowWhenDeletingNonExistentProduct(){
        Long nonExistingId = 1L;
        given(productRepository.existsById(nonExistingId)).willReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(nonExistingId));

        verify(productRepository, times(1)).existsById(nonExistingId);
  
    }







}