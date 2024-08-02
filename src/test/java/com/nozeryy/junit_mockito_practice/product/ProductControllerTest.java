package com.nozeryy.junit_mockito_practice.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.List;
import static java.math.BigDecimal.valueOf;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private List<ProductResponse> productResponseList;

    @BeforeEach
    void setUp(){
        productResponse = new ProductResponse(1L, "PN", "PD", valueOf(200), "PC");
        productRequest = new ProductRequest(null, "PN", "PD", valueOf(200), "PC");
        productResponseList = List.of(
                new ProductResponse(1L, "PN1", "PD1", valueOf(200), "PC1"),
                new ProductResponse(2L, "PN2", "PD2", valueOf(200), "PC2"),
                new ProductResponse(3L, "PN3", "PD3", valueOf(200), "PC3")
        );
    }

    @Test
    void testSaveProduct() throws Exception{
        //arrange
        given(productService.createProduct(productRequest)).willReturn(productResponse);

        //act
        ResultActions resultActions = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));
        
        resultActions.andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                     .andExpect(jsonPath("$.id").value(productResponse.id().intValue()))
                     .andExpect(jsonPath("$.name").value(productResponse.name()))
                     .andExpect(jsonPath("$.description").value(productResponse.description()))
                     .andExpect(jsonPath("$.price").value(productResponse.price().intValue()))
                     .andExpect(jsonPath("$.category").value(productResponse.category()));
    }

    @Test
    void testGetProductById() throws Exception {
        Long id = 1L;
        given(productService.findProductById(id)).willReturn(productResponse);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/products/{id}", id));

        resultActions.andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                     .andExpect(jsonPath("$.id").value(productResponse.id().intValue()))
                     .andExpect(jsonPath("$.name").value(productResponse.name()))
                     .andExpect(jsonPath("$.description").value(productResponse.description()))
                     .andExpect(jsonPath("$.price").value(productResponse.price().intValue()))
                     .andExpect(jsonPath("$.category").value(productResponse.category()));
    }

    @Test
    void testGetAllProducts() throws Exception{
        given(productService.findAllProduct()).willReturn(productResponseList);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/products"));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(productResponseList.size()));

        verify(productService, times(1)).findAllProduct();
    }

    @Test
    void testUpdateProduct() throws Exception{
        ProductRequest productRequestToUpdate = new ProductRequest( 1L, "PN", "PD", valueOf(200), "PC");
        ProductResponse productResponseFromUpdate = new ProductResponse(1L, "PN1", "PD1", valueOf(200), "PC1");

        given(productService.updateProduct(productRequestToUpdate)).willReturn(productResponseFromUpdate);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequestToUpdate)));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productResponseFromUpdate.id().intValue()))
                .andExpect(jsonPath("$.name").value(productResponseFromUpdate.name()))
                .andExpect(jsonPath("$.description").value(productResponseFromUpdate.description()))
                .andExpect(jsonPath("$.price").value(productResponseFromUpdate.price().intValue()))
                .andExpect(jsonPath("$.category").value(productResponseFromUpdate.category()));
    }

    @Test
    void testDeleteProductById() throws Exception{
        Long idToDelete = 1L;
        willDoNothing().given(productService).deleteProductById(idToDelete);

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/products/{id}", idToDelete));

        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

}