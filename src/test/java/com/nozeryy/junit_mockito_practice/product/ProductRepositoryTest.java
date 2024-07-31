package com.nozeryy.junit_mockito_practice.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp(){
        testProduct = new Product(1L,"PN", "PD1", valueOf(210), "PC");
        productRepository.save(testProduct);
    }

    @Test
    void testSaveProduct(){
        //arrange
        Product product = new Product("PN", "PD1", valueOf(210), "PC");

        //act
        Product saved = productRepository.save(product);

        //assert
        assertThat(saved).isNotNull();
    }

    @Test
    void testFindProductById(){
        Long id = 1L;

        //act
        Optional<Product> fetchProduct = productRepository.findById(id);

        //assert
        assertThat(fetchProduct.get()).isNotNull();
    }

    @Test
    void testFindAllProduct(){
        List<Product> products = List.of(
                new Product(null,"PN1", "PD1", valueOf(210), "PC1"),
                new Product(null,"PN2", "PD2", valueOf(210), "PC2"),
                new Product(null,"PN3", "PD3", valueOf(210), "PC3")
        );
        productRepository.saveAll(products);

        //act
        List<Product> fetchProducts = productRepository.findAll();

        //assert
        assertThat(fetchProducts).hasSize(4);
    }

    @Test
    void testUpdateProduct(){
        testProduct.setPrice(valueOf(200));

        Product updatedProduct = productRepository.save(testProduct);

        BigDecimal expectedPrice = valueOf(200);
        assertThat(updatedProduct.getPrice()).isEqualTo(expectedPrice);
    }

    @Test
    void testDeleteProduct(){
        Long id = 1L;

        productRepository.deleteById(id);

        Product deletedProduct = productRepository.findById(id).orElse(null);

        assertThat(deletedProduct).isNull();

    }

}