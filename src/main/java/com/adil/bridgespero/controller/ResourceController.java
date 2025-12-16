package com.adil.bridgespero.controller;

import com.adil.bridgespero.domain.model.dto.response.ResourceResponse;
import com.adil.bridgespero.domain.model.dto.response.ResourceWithMeta;
import com.adil.bridgespero.service.ResourceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resources")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceController {

    ResourceService resourceService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResource(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean download) {

        ResourceWithMeta wrapper = resourceService.load(id);
        ResourceResponse meta = wrapper.meta();
        Resource file = wrapper.resource();

        String disposition = download ? "" : "inline;";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, meta.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        disposition + " filename=\"" + meta.name() + "\"")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(file);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
