package com.fatihsahin.order_tracking.controller;

import com.fatihsahin.order_tracking.dto.UserDto.UserRequestDto;
import com.fatihsahin.order_tracking.dto.UserDto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface IUserController {
    ResponseEntity<UserResponseDto> getUserById(Long id);
    ResponseEntity<UserResponseDto> createUser(UserRequestDto userRequestDto);
    ResponseEntity<UserResponseDto> updateUser(Long id, UserRequestDto userRequestDto);
    ResponseEntity<Void> deleteUser(Long id);
    ResponseEntity<Page<UserResponseDto>> getAllUsers(int page, int size);
}
