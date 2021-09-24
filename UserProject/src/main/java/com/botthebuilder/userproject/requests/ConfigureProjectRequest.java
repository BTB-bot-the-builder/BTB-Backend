package com.botthebuilder.userproject.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigureProjectRequest {

    @JsonProperty(required = true)
    @NotNull(message = "botName is required")
    private String botName;

    @JsonProperty(required = true)
    @NotNull(message = "description is required")
    private String description;

    @JsonProperty(required = true)
    @NotNull(message = "avatarUrl is required")
    private String avatarUrl;

}
