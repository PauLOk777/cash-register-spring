package com.paulok777.controller;

import com.paulok777.dto.ProductDTO;
import com.paulok777.entity.Measure;
import com.paulok777.entity.Product;
import com.paulok777.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/commodity_expert/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

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
        return "redirect:/commodity_expert/products";
    }

    @PostMapping("/{id}")
    public String changeAmountOfProduct(@RequestParam Long amount, @PathVariable String id) {
        productService.setAmountById(amount, Long.valueOf(id));
        return "redirect:/commodity_expert/products";
    }
}
