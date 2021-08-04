package com.address.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.address.entity.AddressEntity;

@Repository
public interface AddressBookRepository extends MongoRepository<AddressEntity, String>, AddressBookRepositoryCustom {

}
