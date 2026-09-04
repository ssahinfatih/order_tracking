package com.fatihsahin.order_tracking.service;

import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersRequestDto;
import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersResponseDto;
import org.springframework.data.domain.Page;

public interface IOrdersService {

    OrdersResponseDto getOrderById(Long id);

    OrdersResponseDto createOrder(OrdersRequestDto ordersRequestDto);

    OrdersResponseDto updateOrder(Long id, OrdersRequestDto ordersRequestDto);

    void deleteOrder(Long id);

    Page<OrdersResponseDto> getAllOrders(int page, int size);
}
