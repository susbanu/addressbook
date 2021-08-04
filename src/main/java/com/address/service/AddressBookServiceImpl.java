package com.address.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.address.dto.AddressDetailDto;
import com.address.dto.AddressDto;
import com.address.entity.AddressEntity;
import com.address.exception.AddressNotFoundException;
import com.address.exception.MongoDBException;
import com.address.mappers.MapStructMapper;
import com.address.repository.AddressBookRepository;
import com.address.util.CsvUtil;
import com.mongodb.MongoException;

@Service
public class AddressBookServiceImpl implements AddressBookService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressBookServiceImpl.class);
	
	@Autowired
	private MapStructMapper mapstructMapper;

	@Autowired
	private AddressBookRepository addressBookRepository;

	@Override
	public AddressDto saveAddress(AddressDto addressDto) {
		AddressEntity addressEntity = mapstructMapper.addressDtoToAddressEntity(addressDto);
		try {
			return mapstructMapper.addressEntityToAddressDto(addressBookRepository.save(addressEntity));
		} catch (DuplicateKeyException e) {
			throw new MongoDBException("Duplicate key error collection", e);
		} catch (MongoException e) {
			throw new MongoDBException(e);
		}
	}

	@Override
	public AddressDetailDto searchAddress(String addressField) {
		List<AddressEntity> addressEntities = addressBookRepository.findAllByAny(addressField);
		if (addressEntities.isEmpty()) {
			LOGGER.error("Address not found in database for search field : {} ", addressField);
			throw new AddressNotFoundException("Address not found for :: " + addressField);
		} else {
			List<AddressDto> addressDtos = mapstructMapper.addressEntityToAddressDto(addressEntities);
			return new AddressDetailDto(addressDtos);
		}
	}

	@Override
	public List<AddressDto> loadAddress(MultipartFile file) {

		List<AddressDto> addressDto = CsvUtil.csvToAddress(file);
		List<AddressEntity> addressEntities = mapstructMapper.addressDtoToAddressEntity(addressDto);
		try {
			addressBookRepository.insert(addressEntities);
		} catch (DuplicateKeyException e) {
			throw new MongoDBException("Duplicate key error collection", e);
		} catch (MongoException e) {
			throw new MongoDBException(e);
		}
		return addressDto;
	}
}
