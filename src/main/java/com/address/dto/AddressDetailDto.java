package com.address.dto;

import java.io.Serializable;
import java.util.List;

public class AddressDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<AddressDto> addressDtos;

	public AddressDetailDto(List<AddressDto> addressDtos) {
		this.addressDtos = addressDtos;
	}
	
	public List<AddressDto> getAddressDtos() {
		return addressDtos;
	}

	public void setAddressDtos(List<AddressDto> addressDtos) {
		this.addressDtos = addressDtos;
	}
}
