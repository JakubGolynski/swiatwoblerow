package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;

public interface CategoryService {
	public List<String> getCategories();
	public String addCategory(String category);
	public Category getCategory(String name) throws NotFoundExceptionRequest;
}
