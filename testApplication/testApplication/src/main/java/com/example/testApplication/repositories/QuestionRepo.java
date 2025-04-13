package com.example.testApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testApplication.entities.Question;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long>{

}
