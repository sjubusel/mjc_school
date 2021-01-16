package com.epam.esm.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<TestDto> testGet(@RequestBody(required = false) @Valid TestDto testDto) {
        return ResponseEntity.ok().body(testDto);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<TestDto> testPost(@RequestBody(required = false) @Valid TestDto testDto) {
        return ResponseEntity.ok().body(testDto);
    }

    @GetMapping("/errorlog")
    public ResponseEntity<TestDto> testLog() {
        log.error("ERROR");
        return new ResponseEntity<>(new TestDto(1L, "name","secret"), HttpStatus.OK);
    }
}
