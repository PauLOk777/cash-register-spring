package com.paulok777.service;

import com.paulok777.entity.Measure;
import com.paulok777.entity.Product;
import com.paulok777.entity.User;
import com.paulok777.exception.cashRegisterExc.orderExc.NoSuchProductException;
import com.paulok777.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private UserService userService;

    private final Product product = Product.builder()
            .id(1L)
            .code("1234")
            .name("Product")
            .price(100)
            .amount(5L)
            .measure(Measure.BY_QUANTITY)
            .orderProducts(new HashSet<>())
            .build();

    private final String notValid = "not valid";

    @Test
    public void testFindByIdentifierShouldReturnProductByCodeWhenProductWithSameCodeIsExist() {
        String code = "1234";

        when(productRepository.findByCode(code)).thenReturn(Optional.of(product));
        assertEquals(product, productService.findByIdentifier(code));
    }

    @Test
    public void testFindByIdentifierShouldReturnProductByNameWhenProductWithSameNameIsExist() {
        String name = "Product";

        when(productRepository.findByCode(notValid)).thenReturn(Optional.empty());
        when(productRepository.findByName(name)).thenReturn(Optional.of(product));
        assertEquals(product, productService.findByIdentifier(name));
    }

    @Test
    public void testFindByIdentifierShouldThrowNoSuchProductsExceptionWhenProductNotExist() {
        User user = new User();
        user.setUsername("PauL");

        when(userService.getCurrentUser()).thenReturn(user);
        when(productRepository.findByCode(notValid)).thenReturn(Optional.empty());
        when(productRepository.findByName(notValid)).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class, () -> {
            productService.findByIdentifier(notValid);
        });
    }
}