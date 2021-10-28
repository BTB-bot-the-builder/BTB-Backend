package com.botthebuilder.userproject.requests;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ActionRequest {

    private String actionName;
    private String actionUrl;

}
