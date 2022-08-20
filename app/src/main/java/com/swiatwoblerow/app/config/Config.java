package com.swiatwoblerow.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swiatwoblerow.app.dto.CustomerDto;
import com.swiatwoblerow.app.dto.RatingDto;
import com.swiatwoblerow.app.dto.ReviewDto;
import com.swiatwoblerow.app.entity.Customer;
import com.swiatwoblerow.app.entity.Rating;
import com.swiatwoblerow.app.entity.Review;

@Configuration
public class Config {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.typeMap(Customer.class, CustomerDto.class)
			.addMappings(mapper -> mapper.skip(CustomerDto::setPassword))
			.addMappings(mapper -> mapper.skip(CustomerDto::setJwtToken));
		modelMapper.typeMap(Rating.class, RatingDto.class)
			.<String>addMapping(src -> src.getOwner().getUsername(),
					(dest,value) -> dest.setOwnerUsername(value));
		
		modelMapper.typeMap(Review.class, ReviewDto.class)
		.<String>addMapping(src -> src.getOwner().getUsername(),
				(dest,value) -> dest.setOwnerUsername(value));	
	    return modelMapper;
	}
}
