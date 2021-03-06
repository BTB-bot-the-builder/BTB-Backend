package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.exceptionhandling.FileStorageException;
import com.botthebuilder.userproject.exceptionhandling.InvalidFileContentException;
import com.botthebuilder.userproject.exceptionhandling.InvalidFileExtensionException;
import org.springframework.web.multipart.MultipartFile;

public interface FileSystemStorageService {

    void init() throws Exception;

    String saveFile(MultipartFile file, Long userId, Long projectId) throws FileStorageException, InvalidFileContentException, InvalidFileExtensionException;

}
