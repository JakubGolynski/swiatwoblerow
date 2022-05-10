package com.swiatwoblerow.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.repository.CategoryRepository;
import com.swiatwoblerow.app.service.interfaces.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<String> findAll(){
		return categoryRepository.findAll().stream()
		.map(category -> category.getName())
		.collect(Collectors.toList());
	}

}
