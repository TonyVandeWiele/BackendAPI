package com.hepl.backendapi.controller;

import com.hepl.backendapi.dto.generic.OrderDTO;
import com.hepl.backendapi.dto.post.OrderCreateDTO;
import com.hepl.backendapi.exception.ErrorResponse;
import com.hepl.backendapi.service.OrderService;
import com.hepl.backendapi.utils.enumeration.StatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Order management")
@RequestMapping("/v1")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get order by ID")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> fetchOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Get my orders")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/me/orders")
    public ResponseEntity<List<OrderDTO>> fetchAllMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @PreAuthorize("hasRole('DELIVERY_AGENT')")
    @Operation(summary = "Get orders for the connected Delivery Agent", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Orders found")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/me/order/deliveries")
    public ResponseEntity<List<OrderDTO>> fetchDeliveryAgentOrders() {
        return ResponseEntity.ok(orderService.getMyAssignedOrders());
    }


    @Operation(summary = "Get all orders")
    @ApiResponse(responseCode = "200", description = "Orders found")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> fetchAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(summary = "Create a new order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Argument Not Valid or Duplicate Product ID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/order")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO) {
        OrderDTO created = orderService.createOrder(orderCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Update the status of an order", description = "If the status is updated to 'shipped', a tracking occurrence is automatically generated and linked to an order",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Order status updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid parameter type. For StatusEnum, allowed values are: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELED", content = @Content (schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/order/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestParam StatusEnum newStatus) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }

    @PreAuthorize("hasRole('DELIVERY_AGENT')")
    @Operation(summary = "Assign the connected delivery agent to the specified order")
    @ApiResponse(responseCode = "200", description = "Order delivery agent updated successfully")
    @ApiResponse(responseCode = "409", description = "Conflict with another Delivery Agent", content = @Content (schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/me/order/{orderId}/assign-to-me")
    public ResponseEntity<OrderDTO> assignOrderDeliveryAgent(@PathVariable Long orderId) {
        OrderDTO updatedOrder = orderService.assignOrderToCurrentDeliveryAgent(orderId);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(summary = "Delete an order by ID")
    @ApiResponse(responseCode = "204", description = "Order deleted successfully")
    @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
