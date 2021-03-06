package com.botthebuilder.userproject.controllers;

import com.botthebuilder.userproject.entities.Project;
import com.botthebuilder.userproject.entities.User;
import com.botthebuilder.userproject.exceptionhandling.*;
import com.botthebuilder.userproject.requests.ConfigureProjectRequest;
import com.botthebuilder.userproject.requests.CreateProjectRequest;
import com.botthebuilder.userproject.responses.AllProjects;
import com.botthebuilder.userproject.responses.CreateProjectResponse;
import com.botthebuilder.userproject.responses.ProjectWithoutUser;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class ProjectController {


    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    FileSystemStorageService fileSystemStorage;

    @PostMapping("{userId}/project")
    public ResponseEntity<CreateProjectResponse> createProject(@RequestBody @Valid CreateProjectRequest request, @PathVariable Long userId) throws UserNotFoundException , Exception {

        Optional<User> user = userService.findById(userId);
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
                .date(new SimpleDateFormat("dd-MM-yyyy").format(new Date()))
                .state(1)
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

    @PostMapping("{userId}/projects/{projectId}/configure")
    public ResponseEntity<Response> configure(@PathVariable Long projectId, @PathVariable Long userId, @RequestBody @Valid ConfigureProjectRequest request) throws ProjectNotFoundException, InvalidProjectException , Exception {

        Optional<Project> optionalProject = projectService.getById(projectId);

        Project project = null;
        if(optionalProject.isPresent()){
            project = optionalProject.get();
        }
        else{
            throw new ProjectNotFoundException();
        }

        if(project.getUser().getUserId()!=userId){
            throw new InvalidProjectException();
        }

        projectService.configureProject(project, request.getBotName(), request.getDescription(), request.getAvatarUrl());

        Response response = Response.builder()
                .msg("Project configured successfully")
                .status("200")
                .build();
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @PostMapping("{userId}/projects/{projectId}/data")
    public ResponseEntity<Response> addDataFile(@PathVariable Long projectId, @PathVariable Long userId, @RequestParam("file") MultipartFile file ) throws ProjectNotFoundException, InvalidProjectException, InvalidFileExtensionException, InvalidFileContentException, Exception {


        Optional<Project> optionalProject = projectService.getById(projectId);

        Project project = null;
        if(optionalProject.isPresent()){
            project = optionalProject.get();
        }
        else{
            throw new ProjectNotFoundException();
        }

        if(project.getUser().getUserId()!=userId){
            throw new InvalidProjectException();
        }

        String upfile = fileSystemStorage.saveFile(file, userId ,projectId);

        projectService.setStateDataFile(project);

        Response response = Response.builder()
                .status("200")
                .msg("File saved successfully")
                .build();

        return new ResponseEntity<Response>(response,HttpStatus.OK);

    }

    @GetMapping("{userId}/projects")
    public ResponseEntity<AllProjects> getAllProjects(@PathVariable Long userId) throws UserNotFoundException, Exception {


        Optional<User> optionalUser = userService.findById(userId);

        User user = null;
        if(optionalUser.isPresent()){
            user = optionalUser.get();
        }
        else{
            throw new UserNotFoundException();
        }

        List<ProjectWithoutUser> list = projectService.getAllProjects(user);

        AllProjects allProjects = AllProjects.builder()
                .msg("OK")
                .status("200")
                .projects(list)
                .build();

        return new ResponseEntity<>(allProjects, HttpStatus.OK);

    }


}
