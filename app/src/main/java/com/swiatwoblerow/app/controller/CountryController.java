package com.swiatwoblerow.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.CountryDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.service.interfaces.CountryService;

@RestController
@RequestMapping("/countries")
public class CountryController {

	private CountryService countryService;
	
	public CountryController(CountryService countryService) {
		this.countryService = countryService;
	}

	@GetMapping
	public List<CountryDto> getCountries(){
		return countryService.getCountries();
	}
	
	@GetMapping("/{id}")
	public CountryDto getCountry(@PathVariable int id) throws NotFoundExceptionRequest{
		return countryService.getCountry(id);
	}
	
	@PostMapping
	public CountryDto addCountry(@RequestBody CountryDto countryDto) throws AlreadyExistsException {
		return countryService.addCountry(countryDto.getName());
	}
}