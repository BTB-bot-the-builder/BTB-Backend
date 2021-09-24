package com.botthebuilder.userproject.repositories;

import com.botthebuilder.userproject.entities.Action;
import com.botthebuilder.userproject.entities.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ActionRepositoryTest {

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Test
    public void insert(){

        Project p = projectRepository.findAll().get(0);

        Action action = Action.builder()
                .actionName("reminder")
                .project(p)
                .actionDetail("google calendar reminder at 4 pm")
                .build();

        actionRepository.save(action);


    }

}