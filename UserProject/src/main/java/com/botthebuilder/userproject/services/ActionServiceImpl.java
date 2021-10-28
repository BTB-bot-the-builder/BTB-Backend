package com.botthebuilder.userproject.services;

import com.botthebuilder.userproject.entities.Action;
import com.botthebuilder.userproject.entities.Project;
import com.botthebuilder.userproject.repositories.ActionRepository;
import com.botthebuilder.userproject.requests.ActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActionServiceImpl implements ActionService{

    @Autowired
    ActionRepository actionRepository;

    public void loadActions(List<ActionRequest> actionRequestList, Project project){
        for(int i=0;i<actionRequestList.size();i++){
            Action action = Action.builder()
                    .actionName(actionRequestList.get(i).getActionName())
                    .actionDetail(actionRequestList.get(i).getActionUrl())
                    .project(project)
                    .build();

            actionRepository.save(action);
        }
    }

    @Override
    public void loadSingleAction(String actionName, String actionUrl, Project project) {
        Action action = Action.builder()
                .actionName(actionName)
                .actionDetail(actionUrl)
                .project(project)
                .build();

        actionRepository.save(action);
    }

    @Override
    public Optional<Action> getById(Long actionId) {
        return actionRepository.findById(actionId);
    }

    @Override
    public void deleteFromDb(Action action) {
        actionRepository.delete(action);
    }

}
