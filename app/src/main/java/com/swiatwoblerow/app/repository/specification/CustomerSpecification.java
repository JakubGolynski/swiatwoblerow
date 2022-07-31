package com.swiatwoblerow.app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.swiatwoblerow.app.entity.Customer;

public class CustomerSpecification {
	public static Specification<Customer> isFirstNameLike(String firstName) {
		if(firstName.isEmpty()) {
			return (root, query, builder) -> {
				return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.like(builder.upper(root.<String>get("firstName")),
		    		"%"+firstName.toUpperCase()+"%");
	    };
	}
	
	public static Specification<Customer> isLastNameLike(String lastName) {
		if(lastName.isEmpty()) {
			return (root, query, builder) -> {
				return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.like(builder.upper(root.<String>get("lastName")),
		    		"%"+lastName.toUpperCase()+"%");
	    };
	}
	
	public static Specification<Customer> isUsernameNameLike(String username) {
		if(username.isEmpty()) {
			return (root, query, builder) -> {
				return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.like(builder.upper(root.<String>get("username")),
		    		"%"+username.toUpperCase()+"%");
	    };
	}
	
	public static Specification<Customer> isCityEqual(String city) {
		if(city.isEmpty()) {
			return (root, query, builder) -> {
			    return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.equal(root.<String>get("address").get("city"), city);
	    };
	}
}
