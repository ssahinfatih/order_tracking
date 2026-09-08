package com.fatihsahin.order_tracking.service.impl;

import com.fatihsahin.order_tracking.dto.UserDto.UserRequestDto;
import com.fatihsahin.order_tracking.dto.UserDto.UserResponseDto;
import com.fatihsahin.order_tracking.entities.User;
import com.fatihsahin.order_tracking.exception.NotFoundException;
import com.fatihsahin.order_tracking.mapper.UserMapper;
import com.fatihsahin.order_tracking.repository.UserRepository;
import com.fatihsahin.order_tracking.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        log.info("UserServiceImpl initialized with UserRepository and UserMapper");
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Cacheable(value = "users", key = "#id")//cache önbellekten her zaman aynı id ile gelen requestlerde veritabanına gitmeden cacheten veriyi döndürür.
    @Override
    public UserResponseDto getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Bulunamadı: "+ id));
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.info("Creating user with email: {}", userRequestDto.email());
        User user = userMapper.toUser(userRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }

    @CacheEvict(value = "users",allEntries = true)//cacheten siler. çünkü update edilen userin idsi değişmiş olabilir ve cachedeki eski veriyi silmek gerekir.
    @CachePut(value = "users", key = "#id")
    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Bulunamadı: " + id));
        userMapper.updateUserFromDto(userRequestDto, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(updatedUser);
    }
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "userss", allEntries = true)
    })
    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }
    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size) {
        log.info("Fetching all users with pagination - Page: {}, Size: {}", page, size);
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(userMapper::toUserResponseDto);
    }
}
