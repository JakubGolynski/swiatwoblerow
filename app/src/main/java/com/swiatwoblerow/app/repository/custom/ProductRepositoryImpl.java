package com.swiatwoblerow.app.repository.custom;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import com.swiatwoblerow.app.entity.Product;
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
		Predicate isPriceBetween = builder.between(root.<Double>get("price"),
				productFilter.getPriceFrom(), productFilter.getPriceTo());
		Predicate finalPredicate = isPriceBetween;
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
			Predicate isCategoryEqual = builder.equal(root.<String>get("category").get("name"),
					productFilter.getCategory());
			finalPredicate = builder.and(finalPredicate,isCategoryEqual);
		}
		
		if(productFilter.getCity()!=null) {
			Predicate isCityEqual = builder.equal(root.<String>get("owner").get("address").get("city"),
					productFilter.getCity());
			finalPredicate = builder.and(finalPredicate,isCityEqual);
		}
		
		if(!productFilter.getConditions().isEmpty()) {
			Set<String> conditions = productFilter.getConditions().stream().collect(Collectors.toSet());
			Predicate isConditionMember = root.join("conditions").get("name").in(conditions);
			finalPredicate = builder.and(finalPredicate,isConditionMember);
		}
		
		return finalPredicate;
	}
}
