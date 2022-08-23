package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import com.swiatwoblerow.app.dto.CategoryDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;

public interface CategoryService {
	public List<CategoryDto> getCategories();
	public CategoryDto addCategory(String category) throws AlreadyExistsException;
	public CategoryDto getCategory(String name) throws NotFoundExceptionRequest;
	public CategoryDto getCategory(Integer id) throws NotFoundExceptionRequest;
}
