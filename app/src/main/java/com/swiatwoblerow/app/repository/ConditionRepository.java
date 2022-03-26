package com.swiatwoblerow.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Integer> {
	
}
