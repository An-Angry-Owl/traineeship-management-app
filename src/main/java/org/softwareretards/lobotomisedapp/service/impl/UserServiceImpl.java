package org.softwareretards.lobotomisedapp.service.impl;

import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.mapper.user.UserMapper;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;
import org.softwareretards.lobotomisedapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    // TODO: Add password checker and maker

    private final UserRepository userRepository;
    //private final Integer password;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.password = password;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        // Check if username is available
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        // Create and save base User entity
        User user = new User();
        user.setUsername(userDto.getUsername());
        //user.setPassword();
        user.setRole(userDto.getRole());
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public UserDto getProfile(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::toDto)
                .orElse(null);
    }
}