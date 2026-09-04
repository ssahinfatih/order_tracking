package com.fatihsahin.order_tracking.controller.impl;

import com.fatihsahin.order_tracking.controller.IUserController;
import com.fatihsahin.order_tracking.dto.UserDto.UserRequestDto;
import com.fatihsahin.order_tracking.dto.UserDto.UserResponseDto;
import com.fatihsahin.order_tracking.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserControllerImpl implements IUserController {

    private final IUserService userService;

    public UserControllerImpl(IUserService userService) {
        this.userService = userService;
    }


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto userById = userService.getUserById(id);
        return ResponseEntity.ok().body(userById);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userService.createUser(userRequestDto);
        return ResponseEntity.ok().body(createdUser);
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok().body(updatedUser);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<UserResponseDto> users = userService.getAllUsers(page, size);
        return ResponseEntity.ok().body(users);
    }
}
