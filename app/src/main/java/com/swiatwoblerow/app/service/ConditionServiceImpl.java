package com.swiatwoblerow.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.swiatwoblerow.app.dto.ConditionDto;
import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.ConditionRepository;
import com.swiatwoblerow.app.service.interfaces.ConditionService;

@Service
public class ConditionServiceImpl implements ConditionService {
	
	private ConditionRepository conditionRepository;
	private ModelMapper modelMapper;
	
	public ConditionServiceImpl(ConditionRepository conditionRepository, ModelMapper modelMapper) {
		this.conditionRepository = conditionRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<ConditionDto> getConditions() {
		return conditionRepository.findAll().stream()
				.map(condition -> modelMapper.map(condition, ConditionDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ConditionDto getCondition(Integer id) throws NotFoundExceptionRequest {
		Condition condition = conditionRepository.findById(id).orElseThrow(
				() -> new NotFoundExceptionRequest("Condition with id "+
						id+" does not exist"));
		return modelMapper.map(condition, ConditionDto.class);
	}
	
	@Override
	public ConditionDto addCondition(String conditionName) throws AlreadyExistsException {
		boolean isFound = conditionRepository.existsByName(conditionName);
		if(isFound == true) {
			throw new AlreadyExistsException("condition with name "+
					conditionName+" already exists in database");
		}
		Condition condition = new Condition(conditionName);
		conditionRepository.save(condition);
		return modelMapper.map(condition, ConditionDto.class);
	}
	
	@Override
	public void deleteCondition(int conditionId) throws NotFoundExceptionRequest {
		boolean isFound =  conditionRepository.existsById(conditionId);
		if(isFound == false) {
			throw new NotFoundExceptionRequest("Condition with id "+
					conditionId+" does not exist");
		}
		conditionRepository.deleteById(conditionId);
	}
}
