package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.*;
import com.hepl.backendapi.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MainApiControllerTest {

    @Mock
    private ProductService productService;
    
    @Mock
    private CategoryService categoryService;

    @Mock
    private StockService stockService;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private MainApiController mainApiController;

    private MockMvc mockMvc;

    private ProductDTO productDTO;
    private CategoryDTO categoryDTO;
    private StockDTO stockDTO;
    private UserDTO userDTO;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mainApiController).build();

        // Setup dummy data
        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Test Category");

        stockDTO = new StockDTO();
        stockDTO.setId(1L);
        stockDTO.setQuantity(100);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("testuser");

        orderDTO = new OrderDTO();
        orderDTO.setId(1);
        orderDTO.setStatus("Pending");
    }

    @Test
    void testFetchProductById_Success() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productDTO);

        mockMvc.perform(get("/v1/product/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testFetchAllProducts_Success() throws Exception {
        List<ProductDTO> productList = Arrays.asList(productDTO);
        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testCreateProduct_Success() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/v1/product")
                .contentType("application/json")
                .content("{ \"name\": \"Test Product\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    @Test
    void testDeleteProductById_Success() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/v1/product/1"))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProductById(1L);
    }

    @Test
    void testFetchCategoryById_Success() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryDTO);

        mockMvc.perform(get("/v1/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void testFetchAllStocks_Success() throws Exception {
        List<StockDTO> stockList = Arrays.asList(stockDTO);
        when(stockService.getAllStocks()).thenReturn(stockList);

        mockMvc.perform(get("/v1/stocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].quantity").value(100));

        verify(stockService, times(1)).getAllStocks();
    }

    @Test
    void testFetchUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("testuser"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testFetchAllOrders_Success() throws Exception {
        List<OrderDTO> orderList = Arrays.asList(orderDTO);
        when(orderService.getAllOrders()).thenReturn(orderList);

        mockMvc.perform(get("/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("Pending"));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testFetchOrderById_Success() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(orderDTO);

        mockMvc.perform(get("/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("Pending"));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void testFetchAllProductsByOrderId_Success() throws Exception {
        List<ProductDTO> productList = Arrays.asList(productDTO);
        when(productService.getAllProductsByOrderId(1L)).thenReturn(productList);

        mockMvc.perform(get("/v1/order/1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product"));

        verify(productService, times(1)).getAllProductsByOrderId(1L);
    }
}