package com.botthebuilder.userproject.controllers;

import com.botthebuilder.userproject.entities.Action;
import com.botthebuilder.userproject.entities.Project;
import com.botthebuilder.userproject.exceptionhandling.*;
import com.botthebuilder.userproject.requests.ActionRequest;
import com.botthebuilder.userproject.requests.AddActionsRequest;
import com.botthebuilder.userproject.responses.Response;
import com.botthebuilder.userproject.services.ActionService;
import com.botthebuilder.userproject.services.ProjectService;
import org.apache.catalina.startup.CopyParentClassLoaderRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class ActionController {

    @Autowired
    ProjectService projectService;

    @Autowired
    ActionService actionService;

    @PostMapping("{userId}/projects/{projectId}/actions")
    public ResponseEntity<Response> addAction(@RequestBody @Valid AddActionsRequest request, @PathVariable Long userId, @PathVariable Long projectId) throws ProjectNotFoundException, UserNotFoundException, Exception{

        List<ActionRequest> actionList = request.getActionList();
        Optional<Project> optionalProject = projectService.getById(projectId);
        Project project = null;
        if(optionalProject.isPresent()){
            project = optionalProject.get();
        }
        else{
            throw new ProjectNotFoundException();
        }

        if(project.getUser().getUserId()!=userId){
            throw new InvalidUserException();
        }

        actionService.loadActions(actionList, project);

        Response response = Response.builder()
                .status("200")
                .msg("Actions saved successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{userId}/projects/{projectId}/actions/{actionId}")
    public ResponseEntity<Response> deleteAction(@PathVariable Long userId, @PathVariable Long projectId, @PathVariable Long actionId) throws InvalidProjectException, ActionNotFoundException, Exception{
        Optional<Action> optionalAction = actionService.getById(actionId);
        Action action = null;
        if(optionalAction.isPresent()){
            action = optionalAction.get();
        }
        else{
            throw new ActionNotFoundException();
        }

        Project project = action.getProject();
        if(project.getProjectId()!=projectId){
            throw new InvalidActionException();
        }

        if(project.getUser().getUserId()!=userId){
            throw new InvalidProjectException();
        }

        actionService.deleteFromDb(action);

        Response response = Response.builder()
                .status("200")
                .msg("Action deleted successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("{userId}/projects/{projectId}/action")
    public ResponseEntity<Response> addAction(@RequestBody ActionRequest request, @PathVariable Long userId, @PathVariable Long projectId) throws ProjectNotFoundException, InvalidProjectException, Exception{
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

        String actionName = request.getActionName();
        String actionUrl = request.getActionUrl();

        actionService.loadSingleAction(actionName, actionUrl, project);

        Response response = Response.builder()
                .status("200")
                .msg("Action saved successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);



    }




}
