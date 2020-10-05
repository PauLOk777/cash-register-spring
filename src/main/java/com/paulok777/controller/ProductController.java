package com.paulok777.controller;

import com.paulok777.dto.ProductDTO;
import com.paulok777.entity.Measure;
import com.paulok777.entity.Product;
import com.paulok777.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/commodity_expert/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String getProducts(Model model) {
        List<Product> products = productService.getProducts(PageRequest.of(0, 3));
        model.addAttribute("measures", Measure.values());
        model.addAttribute("products", products);
        return "products";
    }

    @PostMapping
    public String createProduct(ProductDTO productDTO) {
        log.info("(username: {}) create product: {}", productDTO,
                SecurityContextHolder.getContext().getAuthentication().getName());
        productService.saveNewProduct(productDTO);
        return "redirect:/commodity_expert/products";
    }

    @PostMapping("/{id}")
    public String changeAmountOfProduct(@RequestParam Long amount, @PathVariable String id) {
        log.info("(username: {}) change amount of product (id:{}) to: {}", id, amount,
                SecurityContextHolder.getContext().getAuthentication().getName());
        productService.setAmountById(amount, Long.valueOf(id));
        return "redirect:/commodity_expert/products";
    }
}
