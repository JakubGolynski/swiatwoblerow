package com.swiatwoblerow.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.CategoryDto;
import com.swiatwoblerow.app.entity.Category;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CategoryRepository;
import com.swiatwoblerow.app.service.interfaces.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	private CategoryRepository categoryRepository;
	
	private ModelMapper modelMapper;

	public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public String addCategory(String category) {
		// jakie exceptions wyrzuca gdy dana kategoria jest juz w bazie ?? ??? ??
		return null;
	}
	
	@Override
	public CategoryDto getCategory(Integer id) throws NotFoundExceptionRequest {
		Category category = categoryRepository.findById(id).orElseThrow(
				() -> new NotFoundExceptionRequest("Category with id "+
						id+" does not exist"));
		return modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public CategoryDto getCategory(String name) throws NotFoundExceptionRequest{
		Category category = categoryRepository.findByName(name).orElseThrow(
				() -> new NotFoundExceptionRequest("Category with name "+
						name+" does not exist"));
		return modelMapper.map(category, CategoryDto.class);
	}
	
	@Override
	public List<CategoryDto> getCategories() {
		return categoryRepository.findAll().stream()
			.map(category -> modelMapper.map(category, CategoryDto.class))
			.collect(Collectors.toList());
	}

	

}
