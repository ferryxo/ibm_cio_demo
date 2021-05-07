package com.cio.ibm.colorgroup.dao;

import java.util.Map;
import java.util.Set;

public interface ColorRepository {
    Set<String> findAllColors();
    String findColorByPerson(String name);
}
