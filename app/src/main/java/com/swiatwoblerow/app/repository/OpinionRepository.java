package com.swiatwoblerow.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Opinion;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Integer> {

}
