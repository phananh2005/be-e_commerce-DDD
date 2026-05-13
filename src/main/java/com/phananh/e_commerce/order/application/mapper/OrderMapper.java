package com.phananh.e_commerce.order.application.mapper;

import com.phananh.e_commerce.order.domain.model.entity.Order;
import com.phananh.e_commerce.order.presentation.dto.response.order.OrderSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "status", source = "status")
    OrderSummaryResponse toOrderSummaryResponse(Order order);
}


