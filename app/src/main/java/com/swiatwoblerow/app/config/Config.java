package com.swiatwoblerow.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.entity.Customer;

@Configuration
public class Config {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.typeMap(Customer.class, CustomerDto.class)
			.addMappings(mapper -> mapper.skip(CustomerDto::setPassword))
			.addMappings(mapper -> mapper.skip(CustomerDto::setJwtToken));
	    return modelMapper;
	}
}
