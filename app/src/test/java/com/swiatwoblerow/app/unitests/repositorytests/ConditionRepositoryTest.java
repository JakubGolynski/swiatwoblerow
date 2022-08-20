package com.swiatwoblerow.app.unitests.repositorytests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.swiatwoblerow.app.entity.Condition;
import com.swiatwoblerow.app.repository.ConditionRepository;

@DataJpaTest
public class ConditionRepositoryTest {

	@Autowired
	private ConditionRepository conditionRepository;
	
	
	@Test
	public void conditionsShouldBeNullIfRepositoryIsEmpty() {
		Set<String> conditions = new HashSet<>();
		Set<Condition> conditionsFromDatabase = conditionRepository.findByNameIn(conditions);
		assertThat(conditionsFromDatabase).isEmpty();
	}
	
	@Test
	public void shouldFindConditionsIfExist() {
		Condition conditionUsed = new Condition("USED");
		Condition conditionDamaged = new Condition("DAMAGED");
		conditionRepository.save(conditionUsed);
		conditionRepository.save(conditionDamaged);
		
		Set<Condition> conditionsExpected = new HashSet<>();
		conditionsExpected.add(conditionUsed);
		conditionsExpected.add(conditionDamaged);
		
		Set<String> conditionNames = new HashSet<>();
		conditionNames.add("USED");
		conditionNames.add("DAMAGED");
		
		Set<Condition> conditionsFromDatabase = conditionRepository.findByNameIn(conditionNames);
		
		assertThat(conditionsFromDatabase).isEqualTo(conditionsExpected);
	}
}
