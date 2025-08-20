package com.antonio.storeinventorysystem;
import com.antonio.storeinventorysystem.controller.ProductController;
import com.antonio.storeinventorysystem.domain.Product;
import com.antonio.storeinventorysystem.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product validProduct() {
        return Product.builder()
                .id(1L)
                .name("Phone")
                .description("Smartphone")
                .price(new BigDecimal("800.00"))
                .quantity(20)
                .build();
    }

    private Product invalidProduct() {
        // invalid because name is blank and price/quantity negative
        return Product.builder()
                .name("")
                .description("x")
                .price(new BigDecimal("-1"))
                .quantity(-5)
                .build();
    }

    // GET /api/products
    @Test
    void getAllProducts_ok() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(validProduct()));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Phone"))
                .andExpect(jsonPath("$[0].price").value(800.00));
    }

    // POST /api/products (201)
    @Test
    void createProduct_created() throws Exception {
        when(productService.saveProduct(any(Product.class)))
                .thenReturn(validProduct());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProduct())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Phone"));
    }

    // POST /api/products validation fails (400)
    @Test
    void createProduct_invalid_returnsBadRequest() throws Exception {
        // The controller uses @Valid; Spring will short-circuit before service call
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct())))
                .andExpect(status().isBadRequest());
    }

    // PUT /api/products/{id} (200)
    @Test
    void updateProduct_ok() throws Exception {
        Product updated = Product.builder()
                .id(1L)
                .name("Phone Pro")
                .description("Upgraded")
                .price(new BigDecimal("999.99"))
                .quantity(10)
                .build();

        when(productService.updateProduct(eq(1L), any(Product.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone Pro"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    // PUT /api/products/{id} not found (404)
    @Test
    void updateProduct_notFound_returns404() throws Exception {
        when(productService.updateProduct(Mockito.eq(99L), any(Product.class)))
                .thenThrow(new RuntimeException("Product with id 99 not found"));

        mockMvc.perform(put("/api/products/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProduct())))
                .andExpect(status().isNotFound());
    }

    // PUT /api/products/{id} validation fails (400)
    @Test
    void updateProduct_invalid_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct())))
                .andExpect(status().isBadRequest());
    }
}
