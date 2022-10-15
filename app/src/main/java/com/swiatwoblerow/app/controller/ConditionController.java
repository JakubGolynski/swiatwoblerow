package com.swiatwoblerow.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.swiatwoblerow.app.dto.ConditionDto;
import com.swiatwoblerow.app.exceptions.AlreadyExistsException;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.service.interfaces.ConditionService;

@RestController
@RequestMapping("/conditions")
public class ConditionController {
	private ConditionService conditionService;

	public ConditionController(ConditionService conditionService) {
		this.conditionService = conditionService;
	}
	
	@GetMapping
	public List<ConditionDto> getConditions(){
		return conditionService.getConditions();
	}

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ConditionDto addCondition(@RequestBody ConditionDto conditionDto) throws AlreadyExistsException {
		return conditionService.addCondition(conditionDto.getName());
	}
	
	@DeleteMapping("/{conditionId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteCondition(@PathVariable int conditionId) throws NotFoundExceptionRequest{
		conditionService.deleteCondition(conditionId);
	}
}
