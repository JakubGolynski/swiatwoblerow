package com.swiatwoblerow.app.config;

import com.swiatwoblerow.app.dto.ConditionDto;
import com.swiatwoblerow.app.dto.ProductDto;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.entity.Product;
import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.dto.RatingDto;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Rating;

@Configuration
public class Config {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		modelMapper.emptyTypeMap(Customer.class, CustomerDto.class)
				.addMappings(mapper -> mapper.skip(CustomerDto::setRole))
				.addMappings(mapper -> mapper.skip(CustomerDto::setAddress))
			.addMappings(mapper -> mapper.skip(CustomerDto::setPassword))
			.addMappings(mapper -> mapper.skip(CustomerDto::setJwtToken))
		.addMappings(mapper -> mapper.skip(CustomerDto::setId))
		.addMappings(mapper -> mapper.skip(CustomerDto::setTelephone))
		.addMappings(mapper -> mapper.skip(CustomerDto::setLastName))
		.addMappings(mapper -> mapper.skip(CustomerDto::setFirstName))
				.implicitMappings();

		modelMapper.typeMap(Rating.class, RatingDto.class)
			.<String>addMapping(src -> src.getOwner().getUsername(),
					(dest,value) -> dest.setOwner(value));

		modelMapper.typeMap(CustomerDto.class, Customer.class)
				.addMappings(mapper -> mapper.skip(Customer::setRole));

		modelMapper.typeMap(Product.class, ProductDto.class)
				.addMapping(src -> src.getOwner().getUsername(),ProductDto::setOwner);

//		modelMapper.typeMap(Condition.class, ConditionDto.class)
//				.addMapping(Condition::getName,ConditionDto::setName);
		
	    return modelMapper;
	}
}
