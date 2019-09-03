package com.cmdelivery.controller;

import com.cmdelivery.service.IStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class FileController {

    private final IStorageService storageService;
    @Value("${partner.image.url}")
    private String partnerImageUrl;

    @GetMapping("/listFiles")
    public String listAllFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                path -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(partnerImageUrl)
                        .path(path.getFileName().toString())
                        .toUriString())
                .collect(Collectors.toList()));

        return "listFiles";
    }

    @GetMapping("${partner.image.url}{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadPartnerImageFile(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename, IStorageService.FileType.MAIN_IMAGE);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("${product.image.url}{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadProductImageFile(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename, IStorageService.FileType.PRODUCT_IMAGE);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("${partner.logo.url}{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadPartnerLogoFile(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename, IStorageService.FileType.LOGO_IMAGE);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}

