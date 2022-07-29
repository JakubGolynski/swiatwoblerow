package com.swiatwoblerow.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CategoryRepository;
import com.swiatwoblerow.app.service.interfaces.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	private CategoryRepository categoryRepository;
	
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public String addCategory(String category) {
		// jakie exceptions wyrzuca gdy dana kategoria jest juz w bazie
		return null;
	}

	@Override
	public Category getCategory(String name) throws NotFoundExceptionRequest{
		return categoryRepository.findByName(name).orElseThrow(
				() -> new NotFoundExceptionRequest("Category with name "+
						name+" does not exist"));
	}
	
	@Override
	public List<String> getCategories() {
		return categoryRepository.findAll().stream()
			.map(category -> category.getName())
			.collect(Collectors.toList());
	}

}
