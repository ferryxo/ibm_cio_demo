package com.cio.ibm.colorgroup.dao;

import com.cio.ibm.colorgroup.models.Membership;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashSet;
import java.util.Set;

public class ColorRepositoryImpl implements ColorRepository {

    @Override
    public Set<String> findAllColors() {
        Set<String> colors = new HashSet<>();
        MongoOperations mongoOps = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), "ibm_cio"));
        MongoCursor<String> result = mongoOps.getCollection("membership").distinct("member.color", String.class).iterator();

        while(result.hasNext()) {
            colors.add(result.next());
        }
        return  colors;
    }

    @Override
    public String findColorByPerson(String name) {
        MongoOperations mongoOps = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), "ibm_cio"));
        Query searchQuery = new Query(Criteria.where("member.name").is(name));
        Membership result = mongoOps.findOne(searchQuery, Membership.class);
        if(result!=null && result.getMember()!=null && !result.getMember().isEmpty()) {
            return result.getMember().get(0).getColor();
        }
        return StringUtils.EMPTY;
    }
}
