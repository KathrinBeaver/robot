package ru.mai.pvk.robot.securingweb.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mai.pvk.robot.securingweb.security.domain.model.User;
import ru.mai.pvk.robot.securingweb.security.domain.model.UserProjects;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProjects, Long> {
    Optional<List<UserProjects>> findByUserId(Long userId);
}