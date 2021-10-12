package com.botthebuilder.backend.repository;

import com.botthebuilder.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    public void test(){
        User user = repository.getByEmail("nonexistentuser@gmail.com");
        System.out.println(user);
    }

}