package com.paulok777.controller;

import com.paulok777.dto.ProductDTO;
import com.paulok777.entity.Measure;
import com.paulok777.entity.Product;
import com.paulok777.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProducts(Model model) {
        List<Product> products = productService.getProducts();
        model.addAttribute("measures", Measure.values());
        model.addAttribute("products", products);
        return "products";
    }

    @PostMapping
    public String createProduct(ProductDTO productDTO) {
        productService.saveNewProduct(productDTO);
        return "redirect:/products";
    }

    @PostMapping("/{id}")
    public String changeAmountOfProduct(@RequestParam Long amount, @PathVariable String id) {
        productService.setAmountById(amount, Long.valueOf(id));
        return "redirect:/products";
    }
}
