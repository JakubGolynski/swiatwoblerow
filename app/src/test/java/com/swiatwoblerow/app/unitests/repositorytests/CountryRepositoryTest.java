package com.swiatwoblerow.app.unitests.repositorytests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.swiatwoblerow.app.entity.Country;
import com.swiatwoblerow.app.exceptions.NotFoundExceptionRequest;
import com.swiatwoblerow.app.repository.CountryRepository;

@DataJpaTest
public class CountryRepositoryTest {

	@Autowired
	private CountryRepository countryRepository;
	
	@Test
	public void countryShouldBeNullIfRepositoryIsEmpty() {
		String countryName = "testCountry";
		Country country = countryRepository.findByName(countryName).orElse(null);
		assertThat(country).isNull();
	}
	
	@Test
	public void shouldThrowExceptionIfRepositoryIsEmpty() throws NotFoundExceptionRequest{
		String countryName = "testCountry";
		assertThatThrownBy(() -> {
			Country country = countryRepository.findByName(countryName).orElseThrow(
					() -> new NotFoundExceptionRequest("Country with name "+
							countryName+" does not exist"));
        })
		.isInstanceOf(NotFoundExceptionRequest.class)
        .hasMessage("Country with name "+countryName+" does not exist");
	}
	
	@Test
	public void shouldFindCountryIfRepositoryIsNotEmpty() {
		String countryName = "testCountry";
		Country country = new Country(countryName);
		countryRepository.save(country);
		Country expectedCountry = countryRepository.findByName(countryName).orElse(null);
		
		assertThat(country).isEqualTo(expectedCountry);
	}
}
