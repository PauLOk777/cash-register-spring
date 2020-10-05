package com.paulok777.controller;

import com.paulok777.dto.ProductDTO;
import com.paulok777.entity.Measure;
import com.paulok777.entity.Product;
import com.paulok777.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
@RequestMapping("/commodity_expert/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String getProducts(Model model,@RequestParam(required = false) Optional<Integer> page,
                              @RequestParam(required = false) Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);
        Page<Product> productPage = productService.getProducts(PageRequest.of(currentPage - 1, pageSize));
        System.out.println(productPage.getTotalElements());
        model.addAttribute("productPage", productPage);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("measures", Measure.values());
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
