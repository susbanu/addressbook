package com.address.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.address.dto.AddressDetailDto;
import com.address.dto.AddressDto;

public interface AddressBookService {

	public AddressDto saveAddress(AddressDto addressDto);
	
	public List<AddressDto> loadAddress(MultipartFile addressDto);
	
	public AddressDetailDto searchAddress(String addressField);
}
