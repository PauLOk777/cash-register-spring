package com.paulok777.service;

import com.paulok777.dto.ProductDTO;
import com.paulok777.entity.Product;
import com.paulok777.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findByOrderByName();
    }

    public void saveNewProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        try {
            product = productRepository.save(product);
            log.debug("(username: {}) Product saved successfully. Product id: {}",
                    product.getId(), SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAmountById(Long amount, Long id) {
        try {
            productRepository.updateAmountById(amount, id);
            log.debug("(username: {}) Set new amount to product was done successfully.",
                    SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        () -> new NoSuchElementException("No products for this identifier"))
        );
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}
