package com.fatihsahin.order_tracking.mapper;

import com.fatihsahin.order_tracking.dto.UserDto.UserRequestDto;
import com.fatihsahin.order_tracking.dto.UserDto.UserResponseDto;
import com.fatihsahin.order_tracking.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequestDto userRequestDto);
    UserResponseDto toUserResponseDto(User user);
    List<UserResponseDto> toUserResponseDtoList(List<User> users);
    void updateUserFromDto(UserRequestDto userRequestDto,@MappingTarget User user);//mapping target eni bir User oluşturma, mevcut User nesnesini güncelle der.
}
