package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.entities.Project;
import com.botthebuilder.userproject.entities.User;
import com.botthebuilder.userproject.exceptionhandling.ProjectNotFoundException;
import com.botthebuilder.userproject.repositories.ProjectRepository;
import com.botthebuilder.userproject.responses.ProjectWithoutUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

    public void saveToDb(Project project);

    public Optional<Project> getById(Long id) ;

    public void configureProject(Project project, String botName, String description, String avatarUrl) ;

    public List<ProjectWithoutUser> getAllProjects(User user) ;

    public void setStateDataFile(Project project);

}
