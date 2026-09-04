package com.fatihsahin.order_tracking.service.impl;

import com.fatihsahin.order_tracking.dto.UserDto.UserRequestDto;
import com.fatihsahin.order_tracking.dto.UserDto.UserResponseDto;
import com.fatihsahin.order_tracking.entities.User;
import com.fatihsahin.order_tracking.exception.NotFoundException;
import com.fatihsahin.order_tracking.mapper.UserMapper;
import com.fatihsahin.order_tracking.repository.UserRepository;
import com.fatihsahin.order_tracking.service.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Bulunamadı: "+ id));
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = userMapper.toUser(userRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Bulunamadı: " + id));
        userMapper.updateUserFromDto(userRequestDto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(userMapper::toUserResponseDto);
    }
}
