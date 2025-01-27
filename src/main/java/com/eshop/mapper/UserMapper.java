package com.eshop.mapper;

import com.eshop.dto.UserDto;
import com.eshop.entity.User;
import com.eshop.utils.StringTools;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getIsAdmin()
        );
    }

    public static User mapToUser(UserDto userDto, String encryptedPass) {
        if(userDto == null) return null;
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setIsAdmin(userDto.getIsAdmin());
//        if(rawPass != null) {
//            user.setPassword(StringTools.encodeByMd5(rawPass));
//        }
        user.setPassword(encryptedPass);
        return user;
    }
}
