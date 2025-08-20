package com.antonio.storeinventorysystem;

import com.antonio.storeinventorysystem.domain.Product;
import com.antonio.storeinventorysystem.repository.ProductRepository;
import com.antonio.storeinventorysystem.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product p1;
    private Product pUpdate;

    @BeforeEach
    void init() {
        p1 = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("1500.00"))
                .quantity(10)
                .build();

        pUpdate = Product.builder()
                .name("Laptop Pro")
                .description("Updated desc")
                .price(new BigDecimal("1800.00"))
                .quantity(5)
                .build();
    }

    @Test
    void getAllProducts_returnsList() {
        when(productRepository.findAll()).thenReturn(List.of(p1));

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.getFirst().getName());
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_found_returnsProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(p1));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_notFound_throws() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productService.getProductById(99L));

        assertTrue(ex.getMessage().contains("99"));
        verify(productRepository).findById(99L);
    }

    @Test
    void saveProduct_persistsAndReturns() {
        when(productRepository.save(p1)).thenReturn(p1);

        Product saved = productService.saveProduct(p1);

        assertEquals("Laptop", saved.getName());
        verify(productRepository).save(p1);
    }

    @Test
    void updateProduct_found_updatesAndReturns() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(productRepository.save(any(Product.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Product updated = productService.updateProduct(1L, pUpdate);

        assertEquals("Laptop Pro", updated.getName());
        assertEquals("Updated desc", updated.getDescription());
        assertEquals(new BigDecimal("1800.00"), updated.getPrice());
        assertEquals(5, updated.getQuantity());
        verify(productRepository).findById(1L);
        verify(productRepository).save(p1);
    }

    @Test
    void updateProduct_notFound_throws() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> productService.updateProduct(1L, pUpdate));

        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }
}
