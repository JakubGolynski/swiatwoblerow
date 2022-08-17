package com.swiatwoblerow.app.unitests.repositorytests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CategoryRepository;

@DataJpaTest
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void categoryShouldBeNullIfRepositoryIsEmpty() {
		String categoryName = "testCategory";
		Category category = categoryRepository.findByName(categoryName).orElse(null);
		assertThat(category).isNull();
	}
	
	@Test
	public void shouldThrowExceptionIfRepositoryIsEmpty() throws NotFoundExceptionRequest{
		String categoryName = "testCategory";
		assertThatThrownBy(() -> {
			Category category = categoryRepository.findByName(categoryName).orElseThrow(
					() -> new NotFoundExceptionRequest("Category with name "+
							categoryName+" does not exist"));
        })
		.isInstanceOf(NotFoundExceptionRequest.class)
        .hasMessage("Category with name "+categoryName+" does not exist");
	}
	
	@Test
	public void shouldFindCategoryIfRepositoryIsNotEmpty() {
		String categoryName = "testCategory";
		Category category = new Category(categoryName);
		categoryRepository.save(category);
		Category expectedCategory = categoryRepository.findByName(categoryName).orElse(null);
		
		assertThat(category).isEqualTo(expectedCategory);
	}
}
