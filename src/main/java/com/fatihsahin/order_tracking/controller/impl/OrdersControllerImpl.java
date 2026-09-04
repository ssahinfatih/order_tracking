package com.fatihsahin.order_tracking.controller.impl;

import com.fatihsahin.order_tracking.controller.IOrdersController;
import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersRequestDto;
import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersResponseDto;
import com.fatihsahin.order_tracking.service.IOrdersService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersControllerImpl implements IOrdersController {

    private final IOrdersService ordersService;

    public OrdersControllerImpl(IOrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<OrdersResponseDto> getOrderById(@PathVariable Long id) {
        OrdersResponseDto orderById = ordersService.getOrderById(id);
        return ResponseEntity.ok().body(orderById);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<OrdersResponseDto> createOrder(
            @Valid @RequestBody OrdersRequestDto ordersRequestDto) {

        OrdersResponseDto createdOrder = ordersService.createOrder(ordersRequestDto);
        return ResponseEntity.ok().body(createdOrder);
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<OrdersResponseDto> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrdersRequestDto ordersRequestDto) {

        OrdersResponseDto updatedOrder =
                ordersService.updateOrder(id, ordersRequestDto);

        return ResponseEntity.ok().body(updatedOrder);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        ordersService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<Page<OrdersResponseDto>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<OrdersResponseDto> orders = ordersService.getAllOrders(page, size);
        return ResponseEntity.ok().body(orders);
    }
}