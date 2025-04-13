package com.example.testApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testApplication.entities.Test;
@Repository
public interface TestRepo extends JpaRepository<Test, Long>{

}
