package com.swiatwoblerow.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
	Optional<Country> findByName(String name);
	boolean existsByName(String name);
}
