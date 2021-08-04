package com.address.repository;

import java.util.List;

import com.address.entity.AddressEntity;

public interface AddressBookRepositoryCustom {

	public List<AddressEntity> findAllByAny(String field);
}
