package com.botthebuilder.userproject.repositories;

import com.botthebuilder.userproject.entities.Project;
import com.botthebuilder.userproject.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository repository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void insert(){

        Optional<User> user = userRepository.findById(1l);

        User u = user.get();

        System.out.println(u.getEmail());

        Project project = Project.builder()
                .user(u)
                .projectName("testhelloproject3")
                .state(0)
                .build();


        repository.save(project);
    }

    @Test
    public void configure(){

        Long id = 7l;

        Optional<Project> project = repository.findById(id);

        Project p = project.get();

        System.out.println(p.getProjectId());

        p.setBotName("test YOLO");
        p.setState(1);
        p.setDescription("test bot 1");
        p.setAvatarUrl("www.google.com");
        System.out.println(p.getBotName());
        repository.save(p);

    }

    @Test
    public void setData(){

        Long id = 7l;

        Optional<Project> project = repository.findById(id);

        Project p = project.get();

        System.out.println(p.getProjectId());

        p.setState(2);
        System.out.println(p.getBotName());
        repository.save(p);

    }

    @Test
    public void setApi(){

        Long id = 7l;

        Optional<Project> project = repository.findById(id);

        Project p = project.get();

        System.out.println(p.getProjectId());

        p.setApiKey("api_key");
        p.setState(3);
        System.out.println(p.getBotName());
        repository.save(p);

    }

}