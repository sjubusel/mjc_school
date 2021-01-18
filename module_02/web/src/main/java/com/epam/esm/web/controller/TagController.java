package com.epam.esm.web.controller;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tags")
@Validated
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody @Valid TagDto certificate) {
        Long createdId = tagService.create(certificate);
        URI location = URI.create(String.format("/tags/%s", createdId));
        return ResponseEntity.created(location).body(tagService.findOne(createdId));
    }

    @GetMapping
    public List<TagDto> read(@RequestBody(required = false) @Valid TagSearchCriteriaDto searchCriteriaDto) {
        return tagService.query(searchCriteriaDto);
    }

    @GetMapping("/{id}")
    public TagDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        return tagService.findOne(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") @Positive @Min(1) Long id,
                                         @RequestBody @Valid TagDto tagDto) {
        tagDto.setId(id);
        tagService.update(tagDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") @Positive @Min(1) Long id) {
        tagService.delete(id);
        return ResponseEntity.ok().build();
    }
}
