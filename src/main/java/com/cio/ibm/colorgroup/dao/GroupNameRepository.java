package com.cio.ibm.colorgroup.dao;

import java.util.Set;

public interface GroupNameRepository {
    Set<String> findGroupNames();
}
