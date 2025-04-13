package com.example.testApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testApplication.entities.Response;

@Repository
public interface ResponseRepo extends JpaRepository<Response,Long>{

}
