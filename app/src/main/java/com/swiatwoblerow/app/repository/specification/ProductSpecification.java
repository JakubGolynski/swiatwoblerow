package com.swiatwoblerow.app.repository.specification;

import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;

public class ProductSpecification {
	
	public static Specification<Product> fetchEagerEntites(){
		return (root, query, builder) -> {
			root.fetch("category",JoinType.LEFT);
			root.fetch("conditions",JoinType.LEFT);
			Fetch<Product,Customer> customerFetch = root.fetch("owner",JoinType.LEFT);
			customerFetch.fetch("address",JoinType.LEFT);
			customerFetch.fetch("roles",JoinType.LEFT);
			customerFetch.fetch("managedCategories",JoinType.LEFT);
			Fetch<Customer,Address> addressJoin = customerFetch.fetch("address",JoinType.LEFT);
			addressJoin.fetch("country",JoinType.LEFT);
			return builder.conjunction();
		};
	}
	
	public static Specification<Product> isNameLike(String name) {
		if(name == null) {
			return (root, query, builder) -> {
				return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.like(builder.upper(root.<String>get("name")),
		    		"%"+name.toUpperCase()+"%");
	    };
	}
	
	public static Specification<Product> isPriceBetween(Double priceFrom, Double priceTo) {
	    return (root, query, builder) -> {
		    return builder.between(root.<Double>get("price"), priceFrom, priceTo);
	    };
	}
	
	public static Specification<Product> isRatingGreaterThan(Double ratingFrom) {
		if(ratingFrom == 0) {
			return (root, query, builder) -> {
				return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.greaterThanOrEqualTo(root.<Double>get("rating"), ratingFrom);
	    };
	}
	
	public static Specification<Product> isCategoryEqual(String category) {
		if(category == null) {
			return (root, query, builder) -> {
				return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.equal(root.<String>get("category").get("name"), category);
	    };
	}
	
	public static Specification<Product> isCityEqual(String city) {
		if(city == null) {
			return (root, query, builder) -> {
			    return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.equal(root.<String>get("owner").get("address").get("city"), city);
	    };
	}
	
	public static Specification<Product> isConditionMember(Set<String> conditions) {
		if(conditions.isEmpty()) {
			return (root, query, builder) -> {
			    return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return root.join("conditions").get("name").in(conditions);
	    };
	}
}
