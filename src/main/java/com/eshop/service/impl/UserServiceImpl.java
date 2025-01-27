package com.eshop.service.impl;

import com.eshop.dto.UserDto;
import com.eshop.entity.User;
import com.eshop.exception.BusinessException;
import com.eshop.exception.ResourceNotFoundException;
import com.eshop.mapper.UserMapper;
import com.eshop.repository.UserRepository;
import com.eshop.service.EmailCodeService;
import com.eshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private EmailCodeService emailCodeService;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " is not found"));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users= userRepository.findAll();
        return users.stream().map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long userId, UserDto updateUser) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User " + userId + " is not found")
        );
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setEmail(updateUser.getEmail());
        User updateUserDto = userRepository.save(user);
        return UserMapper.mapToUserDto(updateUserDto);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " is not found"));

        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto createUser(UserDto userDto, String rawPass, String emailCode) {
        User user_email = userRepository.findByEmail(userDto.getEmail());
        if(null != user_email) {
            try {
                throw new BusinessException("This Email has been registered already");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }

        emailCodeService.verifyEmailCode(userDto.getEmail(), emailCode);
        String encryptedPassword = passwordEncoder.encode(rawPass);
        User user = UserMapper.mapToUser(userDto, encryptedPassword);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            try {
                throw new BusinessException("User not found");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
        boolean isPasswordValid = passwordEncoder.matches(password, user.getPassword());
        if(!isPasswordValid) {
            try {
                throw new BusinessException("Email or Password is not correct");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String email, String password, String emailCode) {
        User user = userRepository.findByEmail(email);
        if(null==user) {
            try {
                throw new BusinessException("Email not found");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
        emailCodeService.verifyEmailCode(email, emailCode);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }
}
