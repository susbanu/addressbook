package com.address;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.address.dto.AddressDetailDto;
import com.address.dto.AddressDto;
import com.address.exception.AddressNotFoundException;
import com.address.exception.MongoDBException;
import com.address.service.AddressBookService;

@SpringBootTest
class AddressBookApplicationTests {

	@Autowired
	private AddressBookService addressBookService;
	
	@Nested
	@DisplayName("Save address test scenarios")
	class SaveAddressTest {
		
		@Test
		@DisplayName("Save address test case")
		void testSaveAddress() {
			
			String expectedFirstName = "Raj";
			String expectedLastName = "chavan";
			String expectedStreetName = "345 street";
			String expectedCityName = "Nagpur";
			String expectedStateName = "MH";
			String expectedZip = "211089";
			
			AddressDto addressDto = new AddressDto("Raj", "chavan", "345 street", "Nagpur", "MH", "211089");
			AddressDto actualAddressDto = addressBookService.saveAddress(addressDto);
			
			assertAll(() -> assertEquals(expectedFirstName, actualAddressDto.getFirstName()),
					  () -> assertEquals(expectedLastName, actualAddressDto.getLastName()),
					  () -> assertEquals(expectedCityName, actualAddressDto.getCity()),
					  () -> assertEquals(expectedStreetName, actualAddressDto.getStreet()),
					  () -> assertEquals(expectedStateName, actualAddressDto.getState()),
					  () -> assertEquals(expectedZip, actualAddressDto.getZip()));
		}
		
		@Test
		@DisplayName("Testing Save address with same first and last name")
		void testSaveAddressWithSameFirstAndLastName() {
			
			AddressDto addressDto = new AddressDto("Raj", "chavan", "345 street1", "Nagpur1", "MH1", "2110891");
			
			assertThrows(MongoDBException.class, 
					() -> addressBookService.saveAddress(addressDto), 
					"Combination of first and last name is unique key, so this test case throw DuplicateKeyException");
		}
	}
	
	@Nested
	@DisplayName("All Search address scenarios")
	class SearchAddressTest {
		
		@Test
		@DisplayName("Testing search address with city name")
		void testSearchAddressWithCityName() {
			
			String searchAddressField = "Jetpur";
			
			String expectedFirstName = "sushant";
			String expectedLastName = "banugariya";
			String expectedCityName = "Jetpur";
			String expectedStateName = "MH";
			String expectedZip = "41022";
			
			AddressDetailDto addressDetailDto = addressBookService.searchAddress(searchAddressField);
			assertAll(() -> assertNotNull(addressDetailDto.getAddressDtos().get(0).getFirstName()),
					() -> assertNotNull(addressDetailDto.getAddressDtos().get(0).getLastName()));
			
			assertAll(() -> assertEquals(expectedFirstName, addressDetailDto.getAddressDtos().get(0).getFirstName()),
					  () -> assertEquals(expectedLastName, addressDetailDto.getAddressDtos().get(0).getLastName()),
					  () -> assertEquals(expectedCityName, addressDetailDto.getAddressDtos().get(0).getCity()),
					  () -> assertEquals(expectedStateName, addressDetailDto.getAddressDtos().get(0).getState()),
					  () -> assertEquals(expectedZip, addressDetailDto.getAddressDtos().get(0).getZip()));
		}
		
		@Test
		@DisplayName("Testing search address with first name")
		void testSearchAddressWithFirstName() {
			
			String searchAddressField = "Sushant";
			
			String expectedFirstName = "sushant";
			String expectedLastName = "banugariya";
			String expectedCityName = "Jetpur";
			String expectedStateName = "MH";
			String expectedZip = "41022";
			
			AddressDetailDto addressDetailDto = addressBookService.searchAddress(searchAddressField);
			assertAll(() -> assertNotNull(addressDetailDto.getAddressDtos().get(0).getFirstName()),
					() -> assertNotNull(addressDetailDto.getAddressDtos().get(0).getLastName()));
			
			assertAll(() -> assertEquals(expectedFirstName, addressDetailDto.getAddressDtos().get(0).getFirstName()),
					  () -> assertEquals(expectedLastName, addressDetailDto.getAddressDtos().get(0).getLastName()),
					  () -> assertEquals(expectedCityName, addressDetailDto.getAddressDtos().get(0).getCity()),
					  () -> assertEquals(expectedStateName, addressDetailDto.getAddressDtos().get(0).getState()),
					  () -> assertEquals(expectedZip, addressDetailDto.getAddressDtos().get(0).getZip()));
		}
		
		@Test
		@DisplayName("Testing search address not found for given search field")
		void testSearchAddressNotPresent() {
			
			String searchAddressField = "Pune";
			
			assertThrows(AddressNotFoundException.class, 
					() -> addressBookService.searchAddress(searchAddressField), 
					"If address is not present in database then throw AddressNotFoundException");
		}
	}
}
