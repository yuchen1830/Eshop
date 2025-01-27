package com.eshop.controller;

import com.eshop.annotation.AccessControl;
import com.eshop.dto.ImageCaptcha;
import com.eshop.dto.UserDto;
import com.eshop.entity.Constant;
import com.eshop.enums.VerifyRegexEnums;
import com.eshop.exception.BusinessException;
import com.eshop.service.EmailCodeService;
import com.eshop.service.UserService;
import com.mysql.cj.Session;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Validated
public class UserController {
    private UserService userService;
    private EmailCodeService emailCodeService;

    /**
     * Add a User -> register
     */
//    @PostMapping
//    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
//        UserDto savedUser = userService.createUser(userDto);
//        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
//    }

    /**
     * Get a User
     */
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Get All Users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Update User
     */
    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId,
                                              @RequestBody UserDto updatedUser) {
        UserDto userDto = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Delete User
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User is deleted");
    }

    @RequestMapping("/captcha")
    public void captcha(HttpServletResponse response, HttpSession session, Integer type) throws IOException {
        ImageCaptcha vCode = new ImageCaptcha(130, 40, 4, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        if (type == null || type == 0) {
            session.setAttribute(Constant.CAPTCHA_KEY, code);
        } else {
            session.setAttribute(Constant.CAPTCHA_KEY_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }

    @RequestMapping("/sendEmailCode")
    @AccessControl(requireLogin = false)
    public ResponseEntity<String> sendEmailCode(HttpSession session,
                                                @Email @NotNull String email,
                                                @Pattern(regexp = "^[A-Za-z0-9]{4}$") @NotNull String captcha,
                                                @NotNull Integer status) {
        try {
            if (!captcha.equalsIgnoreCase((String) session.getAttribute(Constant.CAPTCHA_KEY_EMAIL))) {
                try {
                    throw new BusinessException("reCAPTCHA verification failed");
                } catch (BusinessException e) {
                    throw new RuntimeException(e);
                }
            }
            emailCodeService.sendEmailCode(email, status);
            return ResponseEntity.ok("Code is sent to the email");
        } finally {
            session.removeAttribute(Constant.CAPTCHA_KEY_EMAIL);
        }
    }


    @RequestMapping("/register")
    @AccessControl(requireLogin = false)
    public ResponseEntity<String> register(HttpSession session,
                                           @RequestBody @Validated UserDto userDto,
                                           @Pattern(regexp = Constant.PASSWORD_REGEX, message = "Must contain 8-18 characters with letters, numbers, and symbols") String password,
                                           @NotNull String captcha,
                                           @NotNull String emailCode) {
        try {
            if (captcha.equalsIgnoreCase((String) session.getAttribute(Constant.CAPTCHA_KEY))) {
                throw new BusinessException("reCAPTCHA verification failed");
            }
            userService.createUser(userDto, password, emailCode);
            return ResponseEntity.ok("User registration successful");
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.removeAttribute(Constant.CAPTCHA_KEY);
        }
    }

    @RequestMapping("/login")
    @AccessControl(requireLogin = false)
    public ResponseEntity<UserDto> login(HttpSession session,
                                        String email,
                                        @NotNull String password,
                                        @NotNull String captcha) {
        try {
            if (captcha.equalsIgnoreCase((String) session.getAttribute(Constant.CAPTCHA_KEY))) {
                throw new BusinessException("reCAPTCHA verification failed");
            }
            UserDto userDto = userService.login(email, password);
            if(userDto == null) {
                System.out.println("Login failed: sessionWebUserDto is null");
                throw new BusinessException("Login failed");
            }
            session.setAttribute(Constant.SESSION_KEY, userDto);
            return ResponseEntity.ok(userDto);
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.removeAttribute(Constant.CAPTCHA_KEY);
        }
    }
    @RequestMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("logout successful");
    }

    @RequestMapping("/resetPassword")
    @AccessControl(requireLogin = false)
    public ResponseEntity<String> resetPassword(HttpSession session,
                                           @NotNull String email,
                                           @Pattern(regexp = Constant.PASSWORD_REGEX, message = "Must contain 8-18 characters with letters, numbers, and symbols") String password,
                                           @NotNull String captcha,
                                           @NotNull String emailCode) {
        try {
            if (captcha.equalsIgnoreCase((String) session.getAttribute(Constant.CAPTCHA_KEY))) {
                throw new BusinessException("reCAPTCHA verification failed");
            }
            userService.resetPassword(email, password, emailCode);
            return ResponseEntity.ok("Reset password successful");
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.removeAttribute(Constant.CAPTCHA_KEY);
        }
    }
}
