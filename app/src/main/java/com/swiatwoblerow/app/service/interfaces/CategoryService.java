package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import com.swiatwoblerow.app.dto.CategoryDto;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;

public interface CategoryService {
	public List<CategoryDto> getCategories();
	public String addCategory(String category);
	public CategoryDto getCategory(String name) throws NotFoundExceptionRequest;
	public CategoryDto getCategory(Integer id) throws NotFoundExceptionRequest;
}
