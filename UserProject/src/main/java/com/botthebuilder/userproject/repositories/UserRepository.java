package com.botthebuilder.userproject.repositories;

import com.botthebuilder.userproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(
            "select u from User u where u.email = ?1"
    )
    User getByEmail(String email);

}
