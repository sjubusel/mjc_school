package com.epam.esm.web.controller;

import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
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
@RequestMapping("/gift-certificates")
@Validated
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @PostMapping
    public ResponseEntity<GiftCertificateDto> create(@RequestBody @Valid GiftCertificateDto certificate) {
        Long createdId = giftCertificateService.create(certificate);
        URI location = URI.create(String.format("/gift-certificates/%s", createdId));
        return ResponseEntity.created(location).body(giftCertificateService.findOne(createdId));
    }


    @GetMapping
    public List<GiftCertificateDto> read(@RequestBody(required = false) @Valid
                                                 GiftCertificateSearchCriteriaDto criteriaDto) {
        return giftCertificateService.query(criteriaDto);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        return giftCertificateService.findOne(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") @Positive @Min(1) Long id,
                                         @RequestBody @Valid GiftCertificateDto newCertificate) {
        newCertificate.setId(id);

        if (!giftCertificateService.update(newCertificate)) {
            return ResponseEntity.badRequest().body(String.format("Gift-certificate №%d isn't updated", id));
        }

        return ResponseEntity.ok(String.format("Gift-certificate №%d is successfully updated", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") @Positive @Min(1) Long id) {
        if (giftCertificateService.delete(id)) {
            return ResponseEntity.badRequest().body(String.format("Gift-certificate №%s isn't deleted", id));
        }

        return ResponseEntity.ok(String.format("Gift-certificate №%s is successfully deleted", id));
    }

}
