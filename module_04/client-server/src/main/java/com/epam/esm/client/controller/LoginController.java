package com.epam.esm.client.controller;

import com.epam.esm.client.util.DefaultResponseBodyHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
TODO add HATEOAS
TODO add signUp ???
 */
@RestController
public class LoginController {

    private final String defaultLoginPageBody;

    public LoginController(DefaultResponseBodyHolder responseBodyHolder) {
        this.defaultLoginPageBody = responseBodyHolder.receiveLoginPageBody();
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginPage() {
        return ResponseEntity.ok(defaultLoginPageBody);
    }

}
