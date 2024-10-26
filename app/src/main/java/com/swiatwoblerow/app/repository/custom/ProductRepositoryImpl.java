package com.swiatwoblerow.app.repository.custom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import com.swiatwoblerow.app.entity.*;
import com.swiatwoblerow.app.service.filter.ProductFilter;

public class ProductRepositoryImpl implements ProductRepositoryCustom{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Integer> getProductIdList(ProductFilter productFilter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
		Root<Product> root = query.from(Product.class);
		
		Predicate finalPredicate = createFinalPredicate(builder,root,productFilter);
		
		query.select(root.get("id"));
		query.where(finalPredicate);
		
		return entityManager.createQuery(query)
				.setFirstResult(productFilter.getPage() * productFilter.getSize())
				.setMaxResults(productFilter.getSize())
				.getResultList();
	}
	
	private Predicate createFinalPredicate(CriteriaBuilder builder, Root<Product> root, ProductFilter productFilter) {
		Predicate finalPredicate = builder.conjunction();
		if(productFilter.getPriceFrom()!=0 || productFilter.getPriceTo() != 1000000000) {
			Predicate isPriceBetween = builder.between(root.<Double>get("price"),
					productFilter.getPriceFrom(), productFilter.getPriceTo());
			finalPredicate = builder.and(finalPredicate,isPriceBetween);
		}
		
		if(productFilter.getName()!=null) {
			Predicate isNameLike = builder.like(builder.upper(root.<String>get("name")),
		    		"%"+productFilter.getName().toUpperCase()+"%");
			finalPredicate = builder.and(finalPredicate,isNameLike);
		}
		
		if(productFilter.getRatingFrom()>0) {
			Predicate isRatingGreaterThan = builder.greaterThanOrEqualTo(root.<Double>get("rating"),
					productFilter.getRatingFrom());
			finalPredicate = builder.and(finalPredicate,isRatingGreaterThan);
		}
		
		if(productFilter.getCategory()!=null) {
			Join<Product,Category> category = root.join("category",JoinType.INNER);
			Predicate isCategoryEqual = builder.equal(category.get("name"),
					productFilter.getCategory());
			finalPredicate = builder.and(finalPredicate,isCategoryEqual);
		}
		
		if(productFilter.getCity()!=null) {
			Join<Product,Customer> owner = root.join("owner",JoinType.INNER);
			Join<Product,Address> address = owner.join("address",JoinType.INNER);
			
			Predicate isCityEqual = builder.like(builder.upper(address.get("city")),
					"%"+productFilter.getCity().toUpperCase()+"%");
			finalPredicate = builder.and(finalPredicate,isCityEqual);
		}
		
//		if(!productFilter.getConditions().isEmpty()) {
//			//SetJoin<Product, Condition> conditions = root.join("conditions",JoinType.INNER);
//			Set<String> filterConditions = productFilter.getConditions().stream().collect(Collectors.toSet());
//			Join<Product, Condition> conditions= root.join("conditions",JoinType.INNER);
//			Predicate isConditionMember = builder.isMember(conditions.get("name"),filterConditions);
//			finalPredicate = builder.and(finalPredicate,isConditionMember);
//		}else{
////			Set<String> conditions = new HashSet<>();
////			SetJoin<Product, Condition> conditions =
////			root.fetch("conditions",JoinType.INNER);
////			Predicate addAllConditions = root.joinSet("conditions",JoinType.INNER);
////			finalPredicate = builder.and(finalPredicate, addAllConditions);
//		}
//		root.fetch("conditions",JoinType.LEFT);

		return finalPredicate;
	}
}
