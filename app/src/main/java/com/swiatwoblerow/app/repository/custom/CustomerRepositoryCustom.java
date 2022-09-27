package com.swiatwoblerow.app.repository.custom;

import java.util.List;

import com.swiatwoblerow.app.service.filter.CustomerFilter;

public interface CustomerRepositoryCustom {
	List<Integer> getCustomerIdList(CustomerFilter customerFilter);
}
