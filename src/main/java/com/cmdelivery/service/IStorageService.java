package com.cmdelivery.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {

    enum FileType{
        MAIN_IMAGE, LOGO_IMAGE, PRODUCT_IMAGE
    }

    void init();

    String store(MultipartFile file, String filename, FileType fileType);

    Stream<Path> loadAll();

    Path load(String filename, FileType fileType);

    Resource loadAsResource(String filename, FileType fileType);

    void deleteAll();

}

