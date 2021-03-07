package com.epam.esm.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO delete me
@RestController
public class TestController {

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') and hasRole('ADMIN') " +
            "and (authentication.principal.claims['user_id'] == 1)")
    public String test(Authentication authentication) {
        return "Hello, client!!!";
    }
}
