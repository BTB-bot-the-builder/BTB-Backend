package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.config.FileUploadProperties;
import com.botthebuilder.userproject.exceptionhandling.FileStorageException;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageServiceImpl implements FileSystemStorageService{

    private final Path dirLocation;
    @Autowired
    public FileSystemStorageServiceImpl(FileUploadProperties fileUploadProperties) {
        this.dirLocation = Paths.get(fileUploadProperties.getLocation())
                .toAbsolutePath()
                .normalize();
    }

    @Override
    @PostConstruct
    public void init() throws Exception {
        try {
            Files.createDirectories(this.dirLocation);
        }
        catch (Exception ex) {
            throw new FileStorageException();
        }
    }

    @Override
    public String saveFile(MultipartFile file, Long userId, Long projectId) throws FileStorageException {
        try {
            String fileName = "user-"+userId+"-project-"+projectId+"-"+file.getOriginalFilename();
            Path dfile = this.dirLocation.resolve(fileName);
            Files.copy(file.getInputStream(), dfile,StandardCopyOption.REPLACE_EXISTING);
            return fileName;

        } catch (Exception e) {
            throw new FileStorageException();
        }
    }
}
