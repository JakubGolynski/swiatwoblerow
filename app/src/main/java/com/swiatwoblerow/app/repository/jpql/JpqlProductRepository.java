package com.swiatwoblerow.app.repository.jpql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.service.filter.ProductFilter;

@Repository
public class JpqlProductRepository {

	private EntityManager entityManager;
	
	private ProductFilter productFilter = new ProductFilter();

	private String query = "";
	
	public JpqlProductRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public List<Product> getProductEntities(List<Integer> productIds){
		String selectQuery = "select distinct p from Product p "
				+ "left join fetch p.conditions "
				+ "left join fetch p.owner own "
				+ "left join fetch own.roles "
				+ "left join fetch own.address adr "
				+ "left join fetch adr.country "
				+ "left join fetch p.category "
				+ "where p.id in (:productIds) "
				+ "order by p.createdAt";
		TypedQuery<Product> typedQuery = entityManager.createQuery(selectQuery,Product.class)
				.setParameter("productIds", productIds);
//				.setParameter("sortBy", this.productFilter.getSort());
		return typedQuery.getResultList();
	}
	
	public TypedQuery<Integer> prepareTypedQuery() {
		TypedQuery<Integer> typedQuery = entityManager.createQuery(this.query,Integer.class);
		setParametersToTypedQuery(typedQuery);
		return typedQuery;
	}
	
	private void setParametersToTypedQuery(TypedQuery<Integer> typedQuery) {
		if(this.productFilter.getName()!=null) {
			typedQuery.setParameter("name", productFilter.getName());
		}
		typedQuery.setParameter("priceFrom", this.productFilter.getPriceFrom());
		typedQuery.setParameter("priceTo", this.productFilter.getPriceTo());
		typedQuery.setParameter("ratingFrom", this.productFilter.getRatingFrom());
		if(this.productFilter.getCategory()!=null) {
			typedQuery.setParameter("category", this.productFilter.getCategory());
		}
		if(this.productFilter.getCity()!=null) {
			typedQuery.setParameter("city", this.productFilter.getCity());
		}
		if(!this.productFilter.getConditions().isEmpty()) {
			typedQuery.setParameter("conditions", this.productFilter.getConditions());
		}
//		typedQuery.setParameter("sortBy", this.productFilter.getSort());
		
		typedQuery.setFirstResult(this.productFilter.getPage() * this.productFilter.getSize());
		typedQuery.setMaxResults(this.productFilter.getSize());
		
	}

	public List<Integer> getProductIds() {
		TypedQuery<Integer> typedQuery = this.prepareTypedQuery();
		return typedQuery.getResultList();
	}
	
	public void prepareStringQuery() {
		this.query = "select p.id from Product p ";
		if(!this.productFilter.getConditions().isEmpty()) {
			this.query = this.query + "join p.conditions con ";
		}
		appendFiltersToStringQuery();
		return;
	}

	private void appendFiltersToStringQuery() {
		this.appendPriceFromFilter();
		this.appendPriceToFilter();
		this.appendNameFilterToStringQuery();
		this.appendRatingFromFilter();
		this.appendCategoryFilter();
		this.appendCityFilter();
		this.appendConditionsFilter();
		return;
	}
	
	public void appendNameFilterToStringQuery() {
		if(this.productFilter.getName()!=null) {
			this.query = this.query + "and lower(p.name) like lower(concat('%', :name,'%')) ";
		}
		return;
	}
	
	public void appendPriceFromFilter() {
		this.query = this.query + "where p.price >= :priceFrom ";
		return;
	}
	
	public void appendPriceToFilter() {
		this.query = this.query + "and p.price <= :priceTo ";
		return;
	}
	
	public void appendRatingFromFilter() {
		this.query = this.query + "and p.rating >= :ratingFrom ";
		return;
	}
	
	public void appendCategoryFilter() {
		if(this.productFilter.getCategory()!=null) {
			this.query = this.query + "and p.category.name like :category ";
		}
		return;
	}
	
	public void appendCityFilter() {
		if(this.productFilter.getCity()!=null) {
			this.query = this.query + "and lower(p.owner.address.city) like "
					+ "lower(concat('%', :city,'%')) ";
		}
		return;
	}
	
	public void appendConditionsFilter() {
		if(!this.productFilter.getConditions().isEmpty()) {
			this.query = this.query + "and con.name in (:conditions)";
		}
		return;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public ProductFilter getProductFilter() {
		return productFilter;
	}

	public void setProductFilter(ProductFilter productFilter) {
		this.productFilter = productFilter;
	}
}
