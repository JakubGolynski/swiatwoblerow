package com.swiatwoblerow.app.repository.custom;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.service.filter.CustomerFilter;

public class CustomerRepositoryImpl implements CustomerRepositoryCustom{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Integer> getCustomerIdList(CustomerFilter customerFilter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
		Root<Customer> root = query.from(Customer.class);
		
		Predicate finalPredicate = createFinalPredicate(builder,root,customerFilter);
		
		query.select(root.get("id"));
		query.where(finalPredicate);

		return entityManager.createQuery(query)
				.setFirstResult(customerFilter.getPage() * customerFilter.getSize())
				.setMaxResults(customerFilter.getSize())
				.getResultList();
	}
	
	private Predicate createFinalPredicate(CriteriaBuilder builder, Root<Customer> root, CustomerFilter customerFilter) {
		Predicate finalPredicate = builder.conjunction();
		if(customerFilter.getUsername()!=null) {
			Predicate isUsernameLike = builder.like(builder.upper(root.<String>get("username")),
		    		"%"+customerFilter.getUsername().toUpperCase()+"%");
			finalPredicate = builder.and(finalPredicate,isUsernameLike);
		}
		
		if(customerFilter.getFirstName() != null) {
			Predicate isFirstNameLike = builder.like(builder.upper(root.<String>get("firstName")),
		    		"%"+customerFilter.getFirstName().toUpperCase()+"%");
			finalPredicate = builder.and(finalPredicate,isFirstNameLike);
		}
		
		if(customerFilter.getLastName() != null) {
			Predicate isLastNameLike = builder.like(builder.upper(root.<String>get("lastName")),
		    		"%"+customerFilter.getLastName().toUpperCase()+"%");
			finalPredicate = builder.and(finalPredicate, isLastNameLike);
		}

		if(customerFilter.getCity()!=null) {
			Join<Product,Address> address = root.join("address",JoinType.INNER);
			Predicate isCityEqual = builder.equal(address.get("city"),
					customerFilter.getCity());
			finalPredicate = builder.and(finalPredicate,isCityEqual);
		}
		
		if(customerFilter.getEmail() != null) {
			Predicate isEmailLike = builder.like(builder.upper(root.<String>get("email")),
					"%"+customerFilter.getEmail().toUpperCase()+"%");
			finalPredicate = builder.and(finalPredicate, isEmailLike);
		}
		return finalPredicate;
	}
}
