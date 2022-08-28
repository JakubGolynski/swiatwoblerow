package com.swiatwoblerow.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.CategoryDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.service.interfaces.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public List<CategoryDto> getCategories(){
		return categoryService.getCategories();
	}
	
	@GetMapping("/{id}")
	public CategoryDto getCategory(@PathVariable int id) throws NotFoundExceptionRequest{
		return categoryService.getCategory(id);
	}
	
	@PostMapping
	public CategoryDto addCategory(@RequestBody CategoryDto categoryDto) throws AlreadyExistsException {
		return categoryService.addCategory(categoryDto.getName());
	}
}
