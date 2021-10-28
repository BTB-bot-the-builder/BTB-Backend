package com.botthebuilder.userproject.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @SequenceGenerator(
            name = "project_sequence",
            sequenceName = "project_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "project_sequence"
    )
    private Long projectId;

    private String projectName;

    private String botName;

    private String description;

    private String avatarUrl;

    private String api;

    private String apiKey;

    private String modelPath;

    private String dataFilePath;

    private Integer state;

    private String date;

    private Integer totalRequests = 0;

    private Integer totalRatings = 0;

    private Integer sumRatings = 0;

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name= "userId",
            referencedColumnName = "userId"
    )
    private User user;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL
    )
    private List<Action> actions;

}
