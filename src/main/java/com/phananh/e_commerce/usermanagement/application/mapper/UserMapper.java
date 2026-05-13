package com.phananh.e_commerce.usermanagement.application.mapper;

import com.phananh.e_commerce.usermanagement.domain.model.entity.User;
import com.phananh.e_commerce.usermanagement.presentation.dto.response.user.CustomerUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "address", source = "address")
    CustomerUserResponse toCustomerUserResponse(User user);

    User toEntity(CustomerUserResponse response);
}


