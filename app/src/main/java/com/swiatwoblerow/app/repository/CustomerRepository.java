package com.swiatwoblerow.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.repository.custom.CustomerRepositoryCustom;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>, CustomerRepositoryCustom {
	Optional<Customer> findByUsername(String username);
	Optional<Customer> findByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
	@EntityGraph(attributePaths = {"address.country"})
	List<Customer> findByIdIn(List<Integer> customerIdList, Sort sortBy);
}
