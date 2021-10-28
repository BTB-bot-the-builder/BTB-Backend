package com.botthebuilder.userproject.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerSet {

    @JsonProperty("question")
    String question;

    @JsonProperty("answer")
    String answer;

}
