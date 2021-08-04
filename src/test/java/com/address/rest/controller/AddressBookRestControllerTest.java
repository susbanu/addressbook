package com.address.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.address.dto.AddressDetailDto;
import com.address.dto.AddressDto;
import com.address.service.AddressBookService;

@SpringBootTest
class AddressBookRestControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private AddressBookService addressBookService;

	@Test
	@DisplayName("Testing import address service")
	void testImportAddress() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "address-book.csv", "text/csv",
				"Address book".getBytes());

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(multipart("/api/address/import").file(file)).andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testing import address service - invalid MIME type")
	void testImportAddressWithInvalidMime() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "address-book.csv", "text/html",
				"Address book".getBytes());

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(multipart("/api/address/import").file(file)).andExpect(status().is4xxClientError());
	}
	
	@Test
	@DisplayName("Testing Search address service")
	void testSearchAddress() throws Exception {
		
		AddressDetailDto addressDetailDto = new AddressDetailDto(null);
		AddressDto address1 = new AddressDto("Raj", "chavan", "345 street1", "Nagpur1", "MH1", "2110891");
		AddressDto address2 = new AddressDto("Pratik", "Solanki", "345 street2", "Delhi", "DL", "411021");
		List<AddressDto> addressDtoList = new ArrayList<>();
		addressDtoList.add(address1);
		addressDtoList.add(address2);
		addressDetailDto.setAddressDtos(addressDtoList);
		String searchField = "Raj"; 
				
		Mockito.when(addressBookService.searchAddress(searchField)).thenReturn(addressDetailDto);

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/api/address?field="+searchField)
				.contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testing search address with empty search field")
	void testSearchAddressForEmptySearchField() throws Exception {
		
		AddressDetailDto addressDetailDto = new AddressDetailDto(null);
		AddressDto address1 = new AddressDto("Raj", "chavan", "345 street1", "Nagpur1", "MH1", "2110891");
		AddressDto address2 = new AddressDto("Pratik", "Solanki", "345 street2", "Delhi", "DL", "411021");
		List<AddressDto> addressDtoList = new ArrayList<>();
		addressDtoList.add(address1);
		addressDtoList.add(address2);
		addressDetailDto.setAddressDtos(addressDtoList);
		String searchField = ""; 
				
		Mockito.when(addressBookService.searchAddress(searchField)).thenReturn(addressDetailDto);

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/api/address?field="+searchField)
				.contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Testing save address")
	void testSaveAddress() throws Exception {
		
		AddressDto pratikAddress = new AddressDto("Pratik", "chavan", "345 street1", "Nagpur1", "MH1", "2110891");
				
		Mockito.when(addressBookService.saveAddress(pratikAddress)).thenReturn(pratikAddress);

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(post("/api/address")
				.contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
	}
}
