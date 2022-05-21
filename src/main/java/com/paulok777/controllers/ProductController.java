package com.paulok777.controllers;

import com.paulok777.dto.ProductDto;
import com.paulok777.entities.Measure;
import com.paulok777.entities.Product;
import com.paulok777.exceptions.CashRegisterException;
import com.paulok777.services.ProductService;
import com.paulok777.services.UserService;
import com.paulok777.utils.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private static final String COMMODITY_EXPERT_PRODUCTS = "/commodity_expert/products";
    private static final String PRODUCTS_PAGE = "products";
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String PRODUCTS_PAGE_DATA_ATTRIBUTE = "productPage";
    private static final String PAGE_NUMBERS_ATTRIBUTE = "pageNumbers";
    private static final String CURRENT_PAGE_ATTRIBUTE = "currentPage";
    private static final String MEASURES_ATTRIBUTE = "measures";

    @Value("${default.current.page}")
    private int defaultCurrentPage;
    @Value("${default.page.size}")
    private int defaultPageSize;

    private final ProductService productService;
    private final UserService userService;

    @GetMapping
    public String getProducts(Model model, @RequestParam(required = false) Optional<Integer> page,
                              @RequestParam(required = false) Optional<Integer> size) {
        int currentPage = page.orElse(defaultCurrentPage);
        int pageSize = size.orElse(defaultPageSize);
        Page<Product> productPage = productService.getProducts(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute(PRODUCTS_PAGE_DATA_ATTRIBUTE, productPage);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute(PAGE_NUMBERS_ATTRIBUTE, pageNumbers);
        }

        model.addAttribute(CURRENT_PAGE_ATTRIBUTE, currentPage);
        model.addAttribute(MEASURES_ATTRIBUTE, Measure.values());
        return PRODUCTS_PAGE;
    }

    @PostMapping
    public String createProduct(ProductDto productDTO) {
        String redirectUrl = REDIRECT_PREFIX + COMMODITY_EXPERT_PRODUCTS;
        log.info("(username: {}) create product: {}",
                userService.getCurrentUser().getUsername(), productDTO);
        try {
            Validator.validateProduct(productDTO);
            productService.saveNewProduct(productDTO);
        } catch (CashRegisterException e) {
            e.setRedirectUrl(redirectUrl);
            throw e;
        }
        return redirectUrl;
    }

    @PostMapping("/{id}")
    public String changeAmountOfProduct(@RequestParam Long amount, @PathVariable String id) {
        String redirectUrl = REDIRECT_PREFIX + COMMODITY_EXPERT_PRODUCTS;
        log.info("(username: {}) change amount of product (id:{}) to: {}", id, amount,
                userService.getCurrentUser().getUsername());
        try {
            Validator.validateAmountForCommodityExpert(amount);
        } catch (CashRegisterException e) {
            e.setRedirectUrl(redirectUrl);
            throw e;
        }
        productService.setAmountById(amount, Long.valueOf(id));
        return redirectUrl;
    }
}
