package com.swiatwoblerow.app.service.interfaces;

import java.util.List;

import com.swiatwoblerow.app.dto.ConditionDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;

public interface ConditionService {
	public List<ConditionDto> getConditions();
	public ConditionDto addCondition(String condition) throws AlreadyExistsException;
	public ConditionDto getCondition(Integer id) throws NotFoundExceptionRequest;
	public void deleteCondition(int conditionId) throws NotFoundExceptionRequest;
}
