package com.fatihsahin.order_tracking.service;

import com.fatihsahin.order_tracking.dto.UserDto.UserRequestDto;
import com.fatihsahin.order_tracking.dto.UserDto.UserResponseDto;
import org.springframework.data.domain.Page;

public interface IUserService {
    UserResponseDto getUserById(Long id);

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

    void deleteUser(Long id);

    Page<UserResponseDto> getAllUsers(int page, int size);
}
