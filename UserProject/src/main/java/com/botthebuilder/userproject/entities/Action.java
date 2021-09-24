package com.botthebuilder.userproject.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "project")
public class Action {

    @Id
    @SequenceGenerator(
            name = "action_sequence",
            sequenceName = "action_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "action_sequence"
    )
    private Long actionId;

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "projectId",
            referencedColumnName = "projectId"
    )
    private Project project;

    private String actionName;

    private String actionDetail;

}
