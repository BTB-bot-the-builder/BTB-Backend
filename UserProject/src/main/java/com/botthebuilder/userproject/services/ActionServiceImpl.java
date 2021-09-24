package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionServiceImpl implements ActionService{

    @Autowired
    ActionRepository actionRepository;

}
