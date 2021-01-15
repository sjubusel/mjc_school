package com.epam.esm.web.controller;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@RequestMapping("/tags")
@Validated
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @ResponseBody
    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody @Valid TagDto certificate) {
        Long createdId = tagService.create(certificate);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.findOne(createdId));
    }

    @ResponseBody
    @GetMapping
    public List<TagDto> readAll(@RequestBody(required = false) @Valid TagSearchCriteriaDto searchCriteriaDto) {
        return tagService.findAll(searchCriteriaDto);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public TagDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        return tagService.findOne(id);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") @Positive @Min(1) Long id,
                                         @RequestBody @Valid TagDto tagDto) {
        tagDto.setId(id);
        tagService.update(tagDto);
        return ResponseEntity.ok(String.format("Tag №%d is successfully updated", id));
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") @Positive @Min(1) Long id) {
        tagService.delete(id);
        return ResponseEntity.ok(String.format("Tag №%s is successfully deleted", id));
    }
}
