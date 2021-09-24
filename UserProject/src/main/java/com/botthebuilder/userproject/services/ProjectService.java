package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.entities.Project;
import com.botthebuilder.userproject.exceptionhandling.ProjectNotFoundException;
import com.botthebuilder.userproject.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface ProjectService {

    public void saveToDb(Project project);

    public Optional<Project> getById(Long id);

    public void addDataFile(Long id, String fileName) throws ProjectNotFoundException;

    public void configureProject(Long id, String botName, String description, String avatarUrl) throws ProjectNotFoundException;

}
