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
		String condition = "NOEXISTS";
		Condition conditionFromDatabase = conditionRepository.findByName(condition);
		assertThat(conditionFromDatabase).isNull();
	}
	
	@Test
	public void shouldFindConditionsIfExist() {
		Condition conditionUsed = new Condition("TEST");
		conditionRepository.save(conditionUsed);

		Condition conditionUsedFromDatabase = conditionRepository.findByName("TEST");
		
		assertThat(conditionUsedFromDatabase).isEqualTo(conditionUsed);
	}
}
