package com.swiatwoblerow.app.repository.custom;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import com.swiatwoblerow.app.entity.*;
import com.swiatwoblerow.app.service.filter.ProductFilter;

public class ProductRepositoryImpl implements ProductRepositoryCustom{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Product> getProductsList(ProductFilter productFilter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> root = query.from(Product.class);
		
		Predicate finalPredicate = createFinalPredicate(builder,root,productFilter);

		root.fetch("owner",JoinType.INNER);
		root.fetch("condition",JoinType.INNER);

		query.select(root);
		query.where(finalPredicate);

		setSortOnQuery(query,builder.asc(root.get(productFilter.getSort())));
		
		return entityManager.createQuery(query)
				.setFirstResult(productFilter.getPage() * productFilter.getSize())
				.setMaxResults(productFilter.getSize())
				.getResultList();
	}

	private void setSortOnQuery(CriteriaQuery<Product> query, Order order){
		query.orderBy(order);
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
		
		if(!productFilter.getConditions().isEmpty()) {
			Join<Product, Condition> condition = root.join("condition",JoinType.INNER);
			Predicate isConditionMember = condition.get("name").in(productFilter.getConditions());
			finalPredicate = builder.and(finalPredicate,isConditionMember);
		}

		return finalPredicate;
	}
}
