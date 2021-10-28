package com.botthebuilder.userproject.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ActionRequest {

    @JsonProperty(required = true)
    @NotNull(message = "actionName is required")
    private String actionName;

    @JsonProperty(required = true)
    @NotNull(message = "actionUrl is required")
    private String actionUrl;

}
