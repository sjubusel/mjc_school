package com.epam.esm.web.controller;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

/**
 * a class which performs REST's CRUD operations on resources called "Tags"
 */
@RestController
@RequestMapping("/tags")
@Validated
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    /**
     * a method which realizes REST's CREATE operation
     *
     * @param certificate an object which represents a resource "tag" that must be created
     *                    in a data source
     * @return an object which represents Http response of CREATE operation,
     * which body contains a newly created resource
     */
    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody @Valid TagDto certificate) {
        Long createdId = tagService.create(certificate);
        URI location = URI.create(String.format("/tags/%s", createdId));
        return ResponseEntity.created(location).body(tagService.findOne(createdId));
    }

    /**
     * a method which realizes REST's READ operation of all resources and resources which correspond to specific
     * search criteria (parameters)
     *
     * @param searchCriteriaDto an object with parameters according to which selection of resources is performed
     * @return a collection of resources which correspond to search parameters
     */
    @GetMapping
    public List<TagDto> read(@RequestBody(required = false) @Valid TagSearchCriteriaDto searchCriteriaDto) {
        return tagService.query(searchCriteriaDto);
    }

    /**
     * a method which realizes REST's READ operation of a specific resource with ID stored in a request path
     *
     * @param id id an identification number of a requested resource
     * @return an object which represents a target resource
     */
    @GetMapping("/{id}")
    public TagDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        return tagService.findOne(id);
    }

    /**
     * a method which realizes REST's UPDATE operation of a specific resource with ID stored in a request path
     *
     * @param id     an identification number of a requested resource
     * @param tagDto an object with new fields of a specified resource
     * @return an object which represent Http response of UPDATE operation, which body contains a newly updated resource
     */
    @PatchMapping("/{id}")
    public ResponseEntity<TagDto> update(@PathVariable("id") @Positive @Min(1) Long id,
                                         @RequestBody @Valid TagDto tagDto) {
        tagDto.setId(id);
        tagService.update(tagDto);

        TagDto updatedTag = tagService.findOne(id);

        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }

    /**
     * a method which realizes REST's DELETE operation of a specific resource with ID stored in a request path
     *
     * @param id an identification number of a resource which should be deleted
     * @return an object which represent Http response of DELETE operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") @Positive @Min(1) Long id) {
        tagService.delete(id);
        return ResponseEntity.ok().build();
    }
}
