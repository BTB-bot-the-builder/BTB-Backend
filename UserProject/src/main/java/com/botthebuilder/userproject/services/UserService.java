package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.entities.User;

import java.util.Optional;

public interface UserService {

    public Optional<User> findById(Long id);

}
