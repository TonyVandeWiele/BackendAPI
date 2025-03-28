package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.*;
import com.hepl.backendapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class MainApiController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final StockService stockService;
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public MainApiController(ProductService productService, CategoryService categoryService, StockService stockService, UserService userService, OrderService orderService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.stockService = stockService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String home() {
        return "Welcome! This is the home page.";
    }

    @GetMapping("/product/id/{id}")
    public ResponseEntity<ProductDTO> fetchProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/products/category/{categoryName}")
    public ResponseEntity<List<ProductDTO>> fetchAllProductsByCategoryName(@PathVariable String categoryName) {
        return ResponseEntity.ok(productService.getAllProductsByCategoryName(categoryName));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> fetchAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO created = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }


    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> fetchAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> fetchCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/stocks")
    public ResponseEntity<List<StockDTO>> fetchAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/stock/{id}")
    public ResponseEntity<StockDTO> fecthStockById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> fetchUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> fetchAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> fetchOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/order/{orderId}/products")
    public ResponseEntity<List<ProductDTO>> fetchAllProductsByOrderId(@PathVariable Long orderId) {
        List<ProductDTO> products = productService.getAllProductsByOrderId(orderId);
        return ResponseEntity.ok(products);
    }
}
