package com.ladv.bitwise.assignment.service;

import com.ladv.bitwise.assignment.domain.User;
import com.ladv.bitwise.assignment.repository.UserRepository;
import com.ladv.bitwise.assignment.service.dto.UserDTO;
import com.ladv.bitwise.assignment.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userMapper
                .toDto(userRepository.findById(id).orElseThrow(() -> new NoSuchElementException()));
    }

    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userMapper.toDto(userRepository.save(user));
    }
}
