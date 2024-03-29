package com.swiatwoblerow.app.service.interfaces;

import java.util.List;


import com.swiatwoblerow.app.dto.CountryDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;

public interface CountryService {
	public List<CountryDto> getCountries();
	public CountryDto getCountry(String name) throws NotFoundExceptionRequest;
	public CountryDto getCountry(Integer id) throws NotFoundExceptionRequest;
	public CountryDto addCountry(String countryName) throws AlreadyExistsException;
	public void deleteCountry(int countryId) throws NotFoundExceptionRequest;
}
