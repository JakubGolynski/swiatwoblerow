package com.swiatwoblerow.app.unitests;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.repository.ProductRepository;

@DataJpaTest
public class RepositoryTests {
	
	
//	@Autowired
//	private ProductRepository productRepository;
//	
//	@Autowired
//	private TestEntityManager entityManager;
//	
//	@Test
//	public void shouldFindNoProductIfRepositoryIsEmpty() {
//		Iterable<Product> product = productRepository.findAll();
//		assertThat(product).isEmpty();
//	}
//	
////	@Test
////	public void shouldSaveProductDetailsInDb() {
////		ProductDetails productDetails = new ProductDetails();
////		productDetails.setMessage("testMessage");
////		productDetails.setQuantity(100);
////		
////		entityManager.persist(productDetails);
////		Optional<ProductDetails> product = productDetailsRepository.findById(
////				productDetails.getId());
////		assertThat(product).isNotEmpty();
////		assertThat(product.get()).isEqualTo(productDetails);
////	}
//	
//	@Test
//	public void shouldSaveProductInDb() {
//		Product product = new Product();
//		product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//		product.setLocalization("Warszawa");
//		product.setName("testProduct");
//		product.setPrice(399.99);
//		
//		entityManager.persist(product);
//		Optional<Product> productFromDb = productRepository.findById(product.getId());
//		assertThat(productFromDb).isNotEmpty();
//		assertThat(productFromDb.get()).isEqualTo(product);
//	}
//	
//	@Test
//	public void shouldFindProductsByExample() {
//		Product product1 = new Product();
//		product1.setLocalization("firstLocalization");
//		product1.setName("firstName");
//		Product product2 = new Product();
//		product2.setName("name");
//		product2.setLocalization("dontmatch");
//		entityManager.persist(product1);
//		entityManager.persist(product2);
//		
//		Product productExample = new Product();
//		productExample.setName("name");
//		productExample.setLocalization("localization");
//		
//		Example<Product> example = Example.of(productExample,ExampleMatcher
//				.matchingAll()
//				.withMatcher("name", ExampleMatcher
//						.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
//				.withMatcher("localization", ExampleMatcher
//						.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
//				.withIgnoreCase());
//		
//		Iterable<Product> expectedProducts = productRepository.findAll(example);
//		
//		assertThat(expectedProducts).hasSize(1).contains(product1);
//	}
//	
//	@Test
//	public void shouldFindProductById() {
//		Product product = new Product();
//		product.setLocalization("nameLocalization");
//		product.setName("nameName");
//		product.setPrice(3999.1);
//		
//		entityManager.persist(product);
//		Optional<Product> findProduct = productRepository.findById(product.getId());
//		assertThat(findProduct).isNotEmpty();
//		assertThat(findProduct.get()).isEqualTo(product);
//	}
}
