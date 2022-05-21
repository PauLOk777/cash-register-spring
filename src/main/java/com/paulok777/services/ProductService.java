package com.paulok777.services;

import com.paulok777.dto.ProductDto;
import com.paulok777.entities.Product;
import com.paulok777.exceptions.cashregister.products.NoSuchProductException;
import com.paulok777.exceptions.cashregister.products.DuplicateCodeOrNameException;
import com.paulok777.repositories.ProductRepository;
import com.paulok777.utils.ExceptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findByOrderByName(pageable);
    }

    public void saveNewProduct(ProductDto productDTO) {
        Product product = new Product(productDTO);
        saveProduct(product);
    }

    public void setAmountById(Long amount, Long id) {
        productRepository.updateAmountById(amount, id);
        log.debug("(username: {}) Set new amount to product was done successfully.",
                userService.getCurrentUser().getUsername());
    }

    public Optional<Product> findByCode(String code) {
        return productRepository.findByCode(code);
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public Product findByIdentifier(String productIdentifier) {
        Optional<Product> product = findByCode(productIdentifier);
        return product.orElseGet(
                () -> findByName(productIdentifier).orElseThrow(
                        () -> {
                            log.error("(username: {}) {}.",
                                    userService.getCurrentUser().getUsername(), ExceptionKeys.NO_SUCH_PRODUCTS);
                            throw new NoSuchProductException(ExceptionKeys.NO_SUCH_PRODUCTS);
                        }));
    }

    public void saveProduct(Product product) {
        try {
            product = productRepository.save(product);
            log.debug("(username: {}) Product saved successfully. Product id: {}",
                    userService.getCurrentUser().getUsername(), product.getId());
        } catch (Exception e) {
            log.error("(username: {}) {}.",
                    userService.getCurrentUser().getUsername(), ExceptionKeys.DUPLICATE_CODE_OR_NAME);
            throw new DuplicateCodeOrNameException(ExceptionKeys.DUPLICATE_CODE_OR_NAME);
        }
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}
