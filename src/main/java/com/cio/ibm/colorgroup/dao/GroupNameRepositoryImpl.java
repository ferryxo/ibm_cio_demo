package com.cio.ibm.colorgroup.dao;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.internal.MongoChangeStreamCursorImpl;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import java.util.HashSet;
import java.util.Set;

public class GroupNameRepositoryImpl implements GroupNameRepository {

    @Override
    public Set<String> findGroupNames() {
        Set<String> names = new HashSet<>();
        MongoOperations mongoOps = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), "ibm_cio"));
        for (String s : mongoOps.getCollection("group").distinct("group", String.class)) {
            names.add(s);
        }
        return  names;
    }
}
