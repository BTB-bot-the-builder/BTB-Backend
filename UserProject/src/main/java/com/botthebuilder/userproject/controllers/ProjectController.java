package com.botthebuilder.userproject.controllers;

import com.botthebuilder.userproject.entities.Project;
import com.botthebuilder.userproject.entities.User;
import com.botthebuilder.userproject.exceptionhandling.FileStorageException;
import com.botthebuilder.userproject.exceptionhandling.ProjectNotFoundException;
import com.botthebuilder.userproject.exceptionhandling.UserNotFoundException;
import com.botthebuilder.userproject.requests.ConfigureProjectRequest;
import com.botthebuilder.userproject.requests.CreateProjectRequest;
import com.botthebuilder.userproject.responses.CreateProjectResponse;
import com.botthebuilder.userproject.responses.Response;
import com.botthebuilder.userproject.services.FileSystemStorageService;
import com.botthebuilder.userproject.services.ProjectService;
import com.botthebuilder.userproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class ProjectController {


    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    FileSystemStorageService fileSystemStorage;

    @PostMapping("user/{userId}/project")
    public ResponseEntity<CreateProjectResponse> createProject(@RequestBody @Valid CreateProjectRequest request, @PathVariable Long userId) throws Exception {

        Optional<User> user = userService.findById(request.getUserId());
        User u = null;

        if(user.isPresent()){
            u = user.get();
        }
        else{
            throw new UserNotFoundException();
        }

        Project project = Project.builder()
                .projectName(request.getProjectName())
                .user(u)
                .state(0)
                .build();

        projectService.saveToDb(project);

        CreateProjectResponse response = CreateProjectResponse.builder()
                .projectId(project.getProjectId())
                .projectName(request.getProjectName())
                .status("200")
                .msg("Project created")
                .build();

        return new ResponseEntity<CreateProjectResponse>(response, HttpStatus.OK);
    }

    @PostMapping("user/{userId}/projects/{projectId}/configure")
    public ResponseEntity<Response> configure(@PathVariable Long projectId, @PathVariable Long userId, @RequestBody @Valid ConfigureProjectRequest request) throws Exception {

        projectService.configureProject(projectId, request.getBotName(), request.getDescription(), request.getAvatarUrl());

        Response response = Response.builder()
                .msg("Project configured successfully")
                .status("200")
                .build();
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @PostMapping("user/{userId}/projects/{projectId}/data")
    public ResponseEntity<Response> addDataFile(@PathVariable Long projectId, @PathVariable Long userId, @RequestParam("file") MultipartFile file ) throws ProjectNotFoundException, FileStorageException {

        String upfile = fileSystemStorage.saveFile(file, userId ,projectId);

        projectService.addDataFile(projectId, upfile);

        Response response = Response.builder()
                .status("200")
                .msg("File saved successfully")
                .build();

        return new ResponseEntity<Response>(response,HttpStatus.OK);

    }


}
