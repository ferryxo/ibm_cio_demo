package com.cio.ibm.colorgroup.dao;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import java.util.HashSet;
import java.util.Set;

public class PeopleRepositoryImpl implements PeopleRepository {

    @Override
    public Set<String> findAllPeople() {
        Set<String> people = new HashSet<>();
        MongoOperations mongoOps = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), "ibm_cio"));
        MongoCursor<String> result = mongoOps.getCollection("membership").distinct("member.name", String.class).iterator();

        while(result.hasNext()) {
            people.add(result.next());
        }
        return  people;
    }
}
