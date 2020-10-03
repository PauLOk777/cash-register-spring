package com.paulok777.service;

import com.paulok777.dto.ProductDTO;
import com.paulok777.entity.Product;
import com.paulok777.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public void saveNewProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        productRepository.save(product);
    }

    public void setAmountById(Long amount, Long id) {
        productRepository.updateAmountById(amount, id);
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
