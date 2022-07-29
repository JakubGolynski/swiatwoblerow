package com.swiatwoblerow.app.service.interfaces;

import com.swiatwoblerow.app.dto.AddressDto;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;

public interface AddressService {
	public AddressDto addAddress(AddressDto addressDto) throws NotFoundExceptionRequest;
}
