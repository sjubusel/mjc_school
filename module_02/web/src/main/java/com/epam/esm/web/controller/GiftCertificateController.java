package com.epam.esm.web.controller;

import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
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
 * a class which performs REST's CRUD operations on a resource called "Gift-certificates"
 */
@RestController
@RequestMapping("/gift-certificates")
@Validated
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    /**
     * a method which realizes REST's CREATE operation
     *
     * @param certificate an object which represents a resource "gift-certificate" that must be created
     *                    in the data source
     * @return an object which represents Http response of CREATE operation,
     * which body contains a newly created resource
     */
    @PostMapping
    public ResponseEntity<GiftCertificateDto> create(@RequestBody @Valid GiftCertificateDto certificate) {
        Long createdId = giftCertificateService.create(certificate);
        URI location = URI.create(String.format("/gift-certificates/%s", createdId));
        return ResponseEntity.created(location).body(giftCertificateService.findOne(createdId));
    }


    /**
     * a method which realizes REST's READ operation of all resources and resources which correspond to specific
     * search criteria (parameters)
     *
     * @param criteriaDto an object with parameters according to which selection of resources is performed
     * @return a collection of resources which correspond to search parameters
     */
    @GetMapping
    public List<GiftCertificateDto> read(@RequestBody(required = false) @Valid
                                                 GiftCertificateSearchCriteriaDto criteriaDto) {
        return giftCertificateService.query(criteriaDto);
    }

    /**
     * a method which realizes REST's READ operation of a specific resource with ID stored in a request path
     *
     * @param id an identification number of a requested resource
     * @return an object which represents a target resource
     */
    @GetMapping("/{id}")
    public GiftCertificateDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        return giftCertificateService.findOne(id);
    }

    /**
     * a method which realizes REST's UPDATE operation of a specific resource with ID stored in a request path
     *
     * @param id             an identification number of a requested resource
     * @param newCertificate an object with new fields of a specified resource
     * @return an object which represent Http response of UPDATE operation, which body contains a newly updated resource
     */
    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> update(@PathVariable("id") @Positive @Min(1) Long id,
                                                     @RequestBody @Valid GiftCertificateDto newCertificate) {
        newCertificate.setId(id);
        giftCertificateService.update(newCertificate);

        GiftCertificateDto updatedGiftCertificate = giftCertificateService.findOne(id);

        return new ResponseEntity<>(updatedGiftCertificate, HttpStatus.OK);
    }

    /**
     * a method which realizes REST's DELETE operation of a specific resource with ID stored in a request path
     *
     * @param id an identification number of a resource which should be deleted
     * @return an object which represents Http response of DELETE operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") @Positive @Min(1) Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
