package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.entities.User;
import com.botthebuilder.userproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    @Override
    public Optional<User> findById(Long id)  {
        return repository.findById(id);
    }
}
