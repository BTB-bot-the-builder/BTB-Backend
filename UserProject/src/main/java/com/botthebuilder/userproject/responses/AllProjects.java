package com.botthebuilder.userproject.responses;

import com.botthebuilder.userproject.entities.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllProjects {

    private String msg;
    private String status;
    List<Project> projects;

}
