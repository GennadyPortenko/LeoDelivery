package com.cmdelivery.service;

import com.cmdelivery.exception.FileNotFoundException;
import com.cmdelivery.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
        import org.springframework.core.io.UrlResource;
        import org.springframework.stereotype.Service;
        import org.springframework.util.FileSystemUtils;
        import org.springframework.util.StringUtils;
        import org.springframework.web.multipart.MultipartFile;

        import javax.annotation.PostConstruct;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.MalformedURLException;
        import java.nio.file.Files;
        import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
        import java.util.stream.Stream;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class FileSystemStorageService implements IStorageService {
    @Value("${storage.location}")
    private String rootLocation;

    private Path rootPath;

    @Override
    @PostConstruct
    public void init() {
        try {
            rootPath = Paths.get(rootLocation);
            Files.createDirectories(rootPath);
        } catch (Exception e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file, String filename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootPath.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        return filename;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootPath, 1)
                    .filter(path -> !path.equals(this.rootPath))
                    .map(this.rootPath::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootPath.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootPath.toFile());
    }
}
