package com.botthebuilder.userproject.responses;

import com.botthebuilder.userproject.entities.Action;
import com.botthebuilder.userproject.entities.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWithoutUser {

    private Long projectId;

    private String projectName;

    private String botName;

    private String description;

    private String avatarUrl;

    private String api;

    private Integer state;

    private String date;

    private List<ActionWithoutProject> actions = new ArrayList<>();

    public ProjectWithoutUser(Project project){
        this.projectId = project.getProjectId();
        this.projectName = project.getProjectName();
        this.botName = project.getBotName();
        this.description = project.getDescription();
        this.avatarUrl = project.getAvatarUrl();
        this.api = "http://localhost:8999/api/chatbot/"+project.getProjectId()+"/chat?api_key={api_key}";
        this.state = project.getState();
        this.date = project.getDate();
        for(Action action: project.getActions()){
            ActionWithoutProject action1 = new ActionWithoutProject(action);
            actions.add(action1);
        }
    }

}
