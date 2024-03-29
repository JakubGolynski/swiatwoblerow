package com.swiatwoblerow.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public CountryDto addCountry(@RequestBody CountryDto countryDto) throws AlreadyExistsException {
		return countryService.addCountry(countryDto.getName());
	}
	
	@DeleteMapping("/{countryId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteCountry(@PathVariable int countryId) throws NotFoundExceptionRequest{
		countryService.deleteCountry(countryId);
	}
}