package com.paulok777.service;

import com.paulok777.dto.ProductDTO;
import com.paulok777.entity.Product;
import com.paulok777.exception.cashRegisterExc.productExc.DuplicateCodeOrNameException;
import com.paulok777.exception.cashRegisterExc.productExc.NotEnoughProductsException;
import com.paulok777.repository.ProductRepository;
import com.paulok777.util.ExceptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    public List<Product> getProducts(Pageable pageable) {
        return productRepository.findByOrderByName(pageable);
    }

    public void saveNewProduct(ProductDTO productDTO) {
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
                            log.warn("(username: {}) {}.",
                                    userService.getCurrentUser().getUsername(),
                                    ExceptionKeys.NO_SUCH_PRODUCTS);
                            throw new NotEnoughProductsException(ExceptionKeys.NO_SUCH_PRODUCTS);
                        })
        );
    }

    public void saveProduct(Product product) {
        try {
            product = productRepository.save(product);
            log.debug("(username: {}) Product saved successfully. Product id: {}",
                    userService.getCurrentUser().getUsername(), product.getId());
        } catch (Exception e) {
            log.warn("(username: {}) {}.",
                    userService.getCurrentUser().getUsername(),
                    ExceptionKeys.DUPLICATE_CODE_OR_NAME);
            throw new DuplicateCodeOrNameException(ExceptionKeys.DUPLICATE_CODE_OR_NAME);
        }
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}
