package com.swiatwoblerow.app.repository.specification;

import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.swiatwoblerow.app.entity.Product;

public class ProductSpecification {
	
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
