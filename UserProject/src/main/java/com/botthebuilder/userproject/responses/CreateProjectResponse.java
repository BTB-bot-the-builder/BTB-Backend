package com.botthebuilder.userproject.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProjectResponse {

    private Long projectId;
    private String projectName;
    private String msg;
    private String status;

}
