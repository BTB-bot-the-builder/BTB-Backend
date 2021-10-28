package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.entities.Action;
import com.botthebuilder.userproject.entities.Project;
import com.botthebuilder.userproject.requests.ActionRequest;

import java.util.List;
import java.util.Optional;

public interface ActionService {

    public void loadActions(List<ActionRequest> requests, Project project);

    public void loadSingleAction(String actionName, String actionUrl,  Project project);

    public Optional<Action> getById(Long actionId);

    public void deleteFromDb(Action action);
}
