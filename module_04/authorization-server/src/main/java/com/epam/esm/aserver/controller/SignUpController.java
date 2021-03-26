package com.epam.esm.aserver.controller;

import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto) {
        encodePassword(userDto);
        Long createdId = userService.create(userDto);
        return ResponseEntity.ok(userService.findOne(createdId));
    }

    private void encodePassword(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
    }
}
