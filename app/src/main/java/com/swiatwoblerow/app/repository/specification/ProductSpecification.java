package com.swiatwoblerow.app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.swiatwoblerow.app.entity.Product;

public class ProductSpecification {
	
	public static Specification<Product> isNameLike(String name) {
		if(name.isEmpty()) {
			return (root, query, builder) -> {
				return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.like(builder.upper(root.<String>get("name")),
		    		"%"+name.toUpperCase()+"%");
	    };
	}
	
	public static Specification<Product> isPriceBetween(Double price_from, Double price_to) {
	    return (root, query, builder) -> {
		    return builder.between(root.<Double>get("price"), price_from, price_to);
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
		if(category.isEmpty()) {
			return (root, query, builder) -> {
				return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.equal(root.<String>get("category").get("name"), category);
	    };
	}
	
	public static Specification<Product> isCityEqual(String city) {
		if(city.isEmpty()) {
			return (root, query, builder) -> {
			    return builder.conjunction();
		    };
		}
	    return (root, query, builder) -> {
		    return builder.equal(root.<String>get("owner").get("address").get("city"), city);
	    };
	}
	
//	public static Specification<Product> isConditionMember(String city) {
//		if(city.isEmpty()) {
//			return (root, query, builder) -> {
//			    return builder.conjunction();
//		    };
//		}
//	    return (root, query, builder) -> {
//		    return builder.equal(root.<String>get("owner").get("address").get("city"), city);
//	    };
//	}
}
