package com.cio.ibm.colorgroup.dao;

import com.cio.ibm.colorgroup.models.Membership;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MembershipRepository extends MongoRepository<Membership, String>, ColorRepository, PeopleRepository, GroupNameRepository {

    @Query(value = "{ 'member.color' : ?0 }", fields = "{'member': {$elemMatch: { 'color': ?0}}}")
    List<Membership> findPeopleByColor(String color);

    @Query(value = "{ 'member.name' : ?0 }", fields = "{'member': {$elemMatch: { 'name': ?0}}}")
    List<Membership> findGroupByPersonName(String person);

    @Query(value = "{ 'member.id' : ?0 }")
    List<Membership> findGroupByPersonId(String person);

    @Query(value = "{ 'group' : ?0 }", fields = "{'member': {$elemMatch: { 'name': ?1}}}")
    List<Membership> findPeopleByGroupAndName(String Group, String name);

    @Query(value = "{ 'group' : ?0 }", fields = "{'member': {$elemMatch: { 'color': ?1}}}")
    List<Membership> findPeopleByGroupAndColor(String Group, String color);

    List<Membership> findByGroup(String group);
}
