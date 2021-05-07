package com.cio.ibm.colorgroup.dao;

import java.util.Set;

public interface PeopleRepository {
    Set<String> findAllPeople();
}
