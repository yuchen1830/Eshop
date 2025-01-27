package com.eshop.service;

import com.eshop.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto, String password, String emailCode);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long userId, UserDto updateUser);
    void deleteUser(Long userId);
    UserDto login(String email, String password);

    void resetPassword(String email, String password, String emailCode);

}
