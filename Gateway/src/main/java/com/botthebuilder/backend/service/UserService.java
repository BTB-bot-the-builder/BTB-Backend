package com.botthebuilder.backend.service;

import com.botthebuilder.backend.entity.User;
import com.botthebuilder.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getUserFromEmail(String email){
        return userRepository.getByEmail(email);
    }

    public void saveToDb(User user){
        userRepository.save(user);
    }

}
