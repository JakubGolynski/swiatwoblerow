package com.swiatwoblerow.app.unitests.repositorytests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Product;
import com.swiatwoblerow.app.entity.Rating;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.RatingRepository;

@DataJpaTest
public class RatingRepositoryTest {
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@Test
	public void ratingShouldBeNullIfRepositoryIsEmpty() {
		Customer customer = new Customer();
		
		
		Product product = new Product();
		
		Rating rating = ratingRepository.findByOwnerAndProduct(customer,product).orElse(null);
		assertThat(rating).isNull();
	}
	
//	@Test
//	public void shouldThrowExceptionIfRepositoryIsEmpty() throws NotFoundExceptionRequest{
//		Customer customer = new Customer();
//		
//		
//		Product product = new Product();
//		assertThatThrownBy(() -> {
//		Rating rating = ratingRepository.findByOwnerAndProduct(customer,product).orElseThrow(
//					() -> new NotFoundExceptionRequest("Rating with Owner "+
//							customer.getUsername()+" and Product id " + product.getId() 
//							+" does not exist"));
//        })
//		.isInstanceOf(NotFoundExceptionRequest.class)
//        .hasMessage("Rating with Owner "+
//				customer.getUsername()+" and Product id " + product.getId() 
//				+" does not exist");
//	}
//	
//	@Test
//	public void shouldFindCategoryIfRepositoryIsNotEmpty() {
//		Customer customer = new Customer();
//		
//		
//		Product product = new Product();
//		
//		Rating rating = new Rating();
//		rating.setOwner(customer);
//		rating.setProduct(product);
//		rating.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//		rating.setValue((short)5);
//		ratingRepository.save(rating);
//		Rating expectedRating = ratingRepository.findByOwnerAndProduct(customer,product).orElse(null);
//		
//		assertThat(rating).isEqualTo(expectedRating);
//	}
}
