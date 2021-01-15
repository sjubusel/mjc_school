package com.epam.esm.web.controller;

import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.service.dto.GiftCertificateSearchCriteriaDto;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@RequestMapping("/gift-certificates")
@Validated
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @ResponseBody
    @PostMapping
    public ResponseEntity<GiftCertificateDto> create(@RequestBody @Valid GiftCertificateDto certificate) {
        Long createdId = giftCertificateService.create(certificate);
        return ResponseEntity.status(HttpStatus.CREATED).body(giftCertificateService.findOne(createdId));
    }


    // TODO rewrite bean validation
    @ResponseBody
    @GetMapping
    public List<GiftCertificateDto> readAll(
            @RequestParam(name = "tags", required = false) List<@Pattern(regexp = "[\\w\\s]{3,256}") String> tags,
            @RequestParam(name = "name", required = false) @Pattern(regexp = "[А-Яа-я]{1,256}") String name,
            @RequestParam(name = "description", required = false)
            @Pattern(regexp = "[-,.!?\\w\\s]{3,1024}") String description,
            @RequestParam(name = "sortParams", required = false)
                    List<@Pattern(regexp = "(name)|(create_date)|(last_update_date)") String> sortParams,
            @RequestParam(name = "order", required = false) @Pattern(regexp = "(DESC)|(ASC)") String order
    ) {
        GiftCertificateSearchCriteriaDto criteriaDto = new GiftCertificateSearchCriteriaDto(tags, name, description,
                sortParams, order);
        return giftCertificateService.findAll(criteriaDto);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public GiftCertificateDto readOne(@PathVariable("id") @Positive @Min(1) Long id) {
        return giftCertificateService.findOne(id);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") @Positive @Min(1) Long id,
                                         @RequestBody @Valid GiftCertificateDto newCertificate) {
        newCertificate.setId(id);
        giftCertificateService.update(newCertificate);
        return ResponseEntity.ok(String.format("Gift-certificate №%d is successfully updated", id));
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") @Positive @Min(1) Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.ok(String.format("Gift-certificate №%s is successfully deleted", id));
    }

}
