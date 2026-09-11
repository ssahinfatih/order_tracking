package com.fatihsahin.order_tracking.service.impl;

import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersRequestDto;
import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersResponseDto;
import com.fatihsahin.order_tracking.entities.Orders;
import com.fatihsahin.order_tracking.exception.NotFoundException;
import com.fatihsahin.order_tracking.mapper.OrdersMapper;
import com.fatihsahin.order_tracking.repository.OrdersRepository;
import com.fatihsahin.order_tracking.service.IOrdersService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrdersServiceImpl implements IOrdersService {

    private final OrdersRepository ordersRepository;
    private final OrdersMapper ordersMapper;


    @Override
    public OrdersResponseDto getOrderById(Long id) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order Bulunamadı: " + id));

        return ordersMapper.toOrdersResponseDto(orders);
    }

    @Override
    public OrdersResponseDto createOrder(OrdersRequestDto ordersRequestDto) {
        Orders orders = ordersMapper.toOrders(ordersRequestDto);

        Orders savedOrders = ordersRepository.save(orders);
        OrdersResponseDto ordersRequestDto1 = ordersMapper.toOrdersResponseDto(savedOrders);
        return ordersRequestDto1;
    }

    @Override
    public OrdersResponseDto updateOrder(Long id, OrdersRequestDto ordersRequestDto) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order Bulunamadı: " + id));

        ordersMapper.updateOrdersFromDto(ordersRequestDto, orders);

        Orders updatedOrders = ordersRepository.save(orders);

        return ordersMapper.toOrdersResponseDto(updatedOrders);
    }

    @Override
    public void deleteOrder(Long id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public Page<OrdersResponseDto> getAllOrders(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        Page<Orders> ordersPage = ordersRepository.findAll(pageable);

        return ordersPage.map(ordersMapper::toOrdersResponseDto);
    }
}
