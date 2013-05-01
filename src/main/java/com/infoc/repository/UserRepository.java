package com.infoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infoc.domain.User;


public interface UserRepository extends JpaRepository<User, Long> {
}
