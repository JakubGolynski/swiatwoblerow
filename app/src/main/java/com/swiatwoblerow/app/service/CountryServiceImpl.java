package com.swiatwoblerow.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.CountryDto;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.service.interfaces.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

	private CountryRepository countryRepository;
	
	private ModelMapper modelMapper;
	
	public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper) {
		this.countryRepository = countryRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<CountryDto> getCountries() {
		return countryRepository.findAll().stream()
				.map(country -> modelMapper.map(country, CountryDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public CountryDto getCountry(String name) throws NotFoundExceptionRequest {
		Country country = countryRepository.findByName(name).orElseThrow(
				() -> new NotFoundExceptionRequest("Country with name "+
						name+" does not exist"));
		return modelMapper.map(country, CountryDto.class);
	}

	@Override
	public CountryDto getCountry(Integer id) throws NotFoundExceptionRequest {
		Country country = countryRepository.findById(id).orElseThrow(
				() -> new NotFoundExceptionRequest("Country with id "+
						id+" does not exist"));
		return modelMapper.map(country, CountryDto.class);
	}
	
	@Override
	public CountryDto addCountry(String countryName) throws AlreadyExistsException {
		boolean isFound = countryRepository.existsByName(countryName);
		if(isFound == true) {
			throw new AlreadyExistsException("country with name "+
					countryName+" already exists in database");
		}
		Country country = new Country(countryName);
		countryRepository.save(country);
		return modelMapper.map(country, CountryDto.class);
	}

	@Override
	public void deleteCountry(int countryId) throws NotFoundExceptionRequest {
		boolean isFound =  countryRepository.existsById(countryId);
		if(isFound == false) {
			throw new NotFoundExceptionRequest("Country with id "+
					countryId+" does not exist");
		}
		countryRepository.deleteById(countryId);
	}

}
