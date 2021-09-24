package com.botthebuilder.userproject.repositories;

import com.botthebuilder.userproject.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
