package com.epam.esm.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') and hasRole('ADMIN')")
    public String test(){

        return "Hello, client!!!";
    }
}
