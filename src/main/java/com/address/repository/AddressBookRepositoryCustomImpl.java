package com.address.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.address.entity.AddressEntity;

@Repository
public class AddressBookRepositoryCustomImpl implements AddressBookRepositoryCustom {

	@Autowired
    private MongoTemplate mongoTemplate;
	
	@Override
	public List<AddressEntity> findAllByAny(String field) {
		
		// Searching with Regular expression" - Partial search
		Query query = Query.query(new Criteria().orOperator(
									Criteria.where("lastName").regex(field, "i"),
					  			  	Criteria.where("firstName").regex(field, "i"),
					  			  	Criteria.where("city").regex(field, "i"),
					  			  	Criteria.where("state").regex(field, "i"),
					  			  	Criteria.where("street").regex(field, "i"),
					  			  	Criteria.where("zip").regex(field, "i")));
		
		// Search with Text index configuration
		//TextCriteria criteria = new TextCriteria().matchingAny(field);
		//Query query = TextQuery.queryText(criteria);
		return mongoTemplate.find(query, AddressEntity.class);
	}
	
}
