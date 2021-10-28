package com.botthebuilder.userproject.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AddActionsRequest {

    @JsonProperty(required = true)
    @NotNull(message = "actionList is required")
    List<ActionRequest> actionList;

}
