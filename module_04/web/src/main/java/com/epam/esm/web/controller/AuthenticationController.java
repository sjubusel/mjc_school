package com.epam.esm.web.controller;

import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.UserService;
import com.epam.esm.service.security.AuthenticationService;
import com.epam.esm.service.dto.impl.UserCredentialsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/singIn")
    public ResponseEntity<Map<String, String>> signIn(UserCredentialsDto credentials) {
        String validJwt = authenticationService.singIn(credentials);

        Map<String, String> body = new HashMap<>();
        body.put("token", validJwt);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/singUp")
    public ResponseEntity<Map<String, Object>> signUp(UserDto user) {
        Long createdUserId = userService.create(user);
        UserDto createdUser = userService.findOne(createdUserId);

        UserCredentialsDto credentials = new UserCredentialsDto(createdUser.getLogin(), createdUser.getPassword());
        String validJwt = authenticationService.singIn(credentials);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", validJwt);
        responseBody.put("created_user", createdUser);

        return ResponseEntity.ok(responseBody);
    }
}