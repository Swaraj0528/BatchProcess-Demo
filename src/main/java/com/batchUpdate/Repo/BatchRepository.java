package com.batchUpdate.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batchUpdate.model.Employee;

@Repository
public interface BatchRepository extends JpaRepository<Employee, Integer> {
    
       
}