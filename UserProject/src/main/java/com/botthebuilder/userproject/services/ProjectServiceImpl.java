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

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public void saveToDb(Project project) {

        projectRepository.save(project);

    }

    @Override
    public Optional<Project> getById(Long id) {

        return projectRepository.findById(id);

    }

    @Override
    public void addDataFile(Long id, String filePath) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);

        Project p = null;

        if(project.isPresent()){
            p = project.get();
        }
        else{
            throw new ProjectNotFoundException();
        }

        p.setDataFilePath(filePath);
        p.setState(2);

        projectRepository.save(p);

    }

    @Override
    public void configureProject(Project p, String botName, String description, String avatarUrl) throws ProjectNotFoundException {


        p.setBotName(botName);
        p.setDescription(description);
        p.setAvatarUrl(avatarUrl);
        p.setState(1);

        projectRepository.save(p);
    }

    public List<ProjectWithoutUser> getAllProjects(User user){
        List<ProjectWithoutUser> list = new ArrayList<>();
        for(int i=0;i<user.getProjectList().size();i++){
            ProjectWithoutUser projectWithoutUser = new ProjectWithoutUser(user.getProjectList().get(i));
            list.add(projectWithoutUser);
        }
        return list;
    }
}
