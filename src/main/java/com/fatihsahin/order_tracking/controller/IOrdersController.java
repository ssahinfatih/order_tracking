package com.fatihsahin.order_tracking.controller;
import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersRequestDto;
import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
public interface IOrdersController {

    ResponseEntity<OrdersResponseDto> getOrderById(Long id);

    ResponseEntity<OrdersResponseDto> createOrder(OrdersRequestDto orderRequestDto);

    ResponseEntity<OrdersResponseDto> updateOrder(Long id, OrdersRequestDto orderRequestDto);

    ResponseEntity<Void> deleteOrder(Long id);

    ResponseEntity<Page<OrdersResponseDto>> getAllOrders(int page, int size);
}