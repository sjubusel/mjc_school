package com.epam.esm.web.controller;

import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.security.AuthenticationService;
import com.epam.esm.service.dto.impl.UserCredentialsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/signIn")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody UserCredentialsDto credentials) {
        String validJwt = authenticationService.singIn(credentials);

        Map<String, String> body = new HashMap<>();
        body.put("token", validJwt);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserDto user) {
        UserDto createdUser = authenticationService.singUp(user);

        UserCredentialsDto credentials = new UserCredentialsDto(createdUser.getLogin(), createdUser.getPassword());
        String validJwt = authenticationService.singIn(credentials);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("created_user", createdUser);
        responseBody.put("token", validJwt);

        return ResponseEntity.ok(responseBody);
    }
}
