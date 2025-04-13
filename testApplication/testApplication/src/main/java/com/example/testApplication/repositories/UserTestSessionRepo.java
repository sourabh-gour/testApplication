package com.example.testApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testApplication.entities.UserTestSession;

@Repository
public interface UserTestSessionRepo extends JpaRepository<UserTestSession, Long>{

}
