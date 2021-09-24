package com.botthebuilder.userproject.repositories;

import com.botthebuilder.userproject.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    public void insert(){
        User user = User.builder()
                .access_token("google_access_token")
                .email("testuser1@gmail.com")
                .username("test user 1")
                .build();

        repository.save(user);
    }



}