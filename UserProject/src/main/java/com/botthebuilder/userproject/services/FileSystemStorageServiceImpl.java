package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.config.FileUploadProperties;
import com.botthebuilder.userproject.exceptionhandling.FileStorageException;
import com.botthebuilder.userproject.exceptionhandling.InvalidFileContentException;
import com.botthebuilder.userproject.exceptionhandling.InvalidFileExtensionException;
import com.botthebuilder.userproject.utils.QuestionAnswerSet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
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

    public void validateFile(MultipartFile file) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String json = file.getBytes().toString();
        List<QuestionAnswerSet> questionAnswerSetList = mapper.readValue(json, new TypeReference<List<QuestionAnswerSet>>(){});
    }

    @Override
    public String saveFile(MultipartFile file, Long userId, Long projectId) throws FileStorageException, InvalidFileContentException, InvalidFileExtensionException {
        try {
            if(!file.getOriginalFilename().endsWith(".json")){
                throw new InvalidFileExtensionException();
            }
            try{
                validateFile(file);
            }
            catch(Exception e){
                throw new InvalidFileContentException();
            }

            String fileName = "user-"+userId+"-project-"+projectId+".json";
            Path dfile = this.dirLocation.resolve(fileName);
            Files.copy(file.getInputStream(), dfile,StandardCopyOption.REPLACE_EXISTING);
            return fileName;

        }
        catch(InvalidFileContentException e){
            throw new InvalidFileContentException();
        }
        catch (InvalidFileExtensionException e){
            throw new InvalidFileExtensionException();
        }
        catch (Exception e) {
            throw new FileStorageException();
        }
    }
}
