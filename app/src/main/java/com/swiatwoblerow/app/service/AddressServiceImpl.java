package com.swiatwoblerow.app.service;

import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.AddressDto;
import com.swiatwoblerow.app.entity.Address;
import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.AddressRepository;
import com.swiatwoblerow.app.repository.CountryRepository;
import com.swiatwoblerow.app.service.interfaces.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
	
	private CountryRepository countryRepository;
	
	private AddressRepository addressRepository;
	
	public AddressServiceImpl(CountryRepository countryRepository, AddressRepository addressRepository) {
		this.countryRepository = countryRepository;
		this.addressRepository = addressRepository;
	}

	@Override
	public AddressDto addAddress(AddressDto addressDto) throws NotFoundExceptionRequest{
		Address address = new Address();
		address.setCity(addressDto.getCity());
		address.setStreet(addressDto.getStreet());
		address.setHouseNumber(addressDto.getHouseNumber());
		Country country = countryRepository.findByName(addressDto.getCountry())
				.orElseThrow(() -> new NotFoundExceptionRequest("Country "+
						"with name "+addressDto.getCountry() +" does not exist"));
		address.setCountry(country);
		addressRepository.save(address);
		return addressDto;
	}
	
}
