package com.epam.esm.web.controller;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagSearchCriteriaDto;
import com.epam.esm.web.util.impl.TagHateoasActionsAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final TagHateoasActionsAppender hateoasActionsAppender;

    /**
     * a method which realizes REST's CREATE operation
     *
     * @param certificate an object which represents a resource "tag" that must be created
     *                    in a data source
     * @return an object which represents Http response of CREATE operation,
     * which body contains a newly created resource
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') and (hasRole('USER') or hasRole('ADMIN'))")
    public CollectionModel<TagDto> read(@RequestBody(required = false) @Valid TagSearchCriteriaDto searchCriteriaDto) {
        List<TagDto> tags = tagService.query(searchCriteriaDto);

        return hateoasActionsAppender.toHateoasCollectionOfEntities(tags);
    }

    /**
     * a method which realizes REST's READ operation of a specific resource with ID stored in a request path
     *
     * @param id id an identification number of a requested resource
     * @return an object which represents a target resource
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') and (hasRole('USER') or hasRole('ADMIN'))")
    public TagDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        TagDto tagDto = tagService.findOne(id);

        hateoasActionsAppender.appendAsForMainEntity(tagDto);

        return tagDto;
    }

    /**
     * a method which realizes REST's UPDATE operation of a specific resource with ID stored in a request path
     *
     * @param id     an identification number of a requested resource
     * @param tagDto an object with new fields of a specified resource
     * @return an object which represent Http response of UPDATE operation, which body contains a newly updated resource
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagDto> update(@PathVariable("id") @Positive @Min(1) Long id,
                                         @RequestBody @Valid TagDto tagDto) {
        tagDto.setId(id);
        tagService.update(tagDto);

        TagDto updatedTag = tagService.findOne(id);

        return ResponseEntity.ok(updatedTag);
    }

    /**
     * a method which realizes REST's DELETE operation of a specific resource with ID stored in a request path
     *
     * @param id an identification number of a resource which should be deleted
     * @return an object which represent Http response of DELETE operation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("id") @Positive @Min(1) Long id) {
        tagService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/main")
    @PreAuthorize("hasAuthority('SCOPE_module_04::read') and hasRole('ADMIN')")
    public List<TagDto> receiveMainTag() {
        List<TagDto> tagList = tagService.receiveMostWidelyUsedTagOfUserWithMaxCostOfOrders();
        tagList.forEach(hateoasActionsAppender::appendSelfReference);
        return tagList;
    }

}
