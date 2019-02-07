package com.ladv.bitwise.assignment.repository;

import com.ladv.bitwise.assignment.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findTop1ByIdIsNotNullOrderByIdDesc();
}
