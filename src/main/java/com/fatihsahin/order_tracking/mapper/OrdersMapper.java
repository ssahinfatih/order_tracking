package com.fatihsahin.order_tracking.mapper;

import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersRequestDto;
import com.fatihsahin.order_tracking.dto.OrdersDto.OrdersResponseDto;
import com.fatihsahin.order_tracking.entities.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrdersMapper {

    Orders toOrders(OrdersRequestDto ordersRequestDto);

    OrdersResponseDto toOrdersResponseDto(Orders orders);

    List<OrdersResponseDto> toOrdersResponseDtoList(List<Orders> orders);

    void updateOrdersFromDto(OrdersRequestDto ordersRequestDto, @MappingTarget Orders orders
    );
}
