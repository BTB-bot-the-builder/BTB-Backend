package com.botthebuilder.userproject.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class ActionWithoutProject {

    Long actionId;
    String actionName;
    String actionUrl;

    public ActionWithoutProject(com.botthebuilder.userproject.entities.Action action){
        actionId = action.getActionId();
        actionName = action.getActionName();
        actionUrl = action.getActionDetail();
    }

    public ActionWithoutProject(){

    }


}
