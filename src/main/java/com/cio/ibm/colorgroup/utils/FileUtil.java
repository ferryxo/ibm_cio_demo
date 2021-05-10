package com.cio.ibm.colorgroup.utils;

import com.cio.ibm.colorgroup.dao.MembershipRepository;
import com.cio.ibm.colorgroup.models.Membership;
import com.cio.ibm.colorgroup.models.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class FileUtil {

    private static MembershipRepository repository;

    @Autowired
    private MembershipRepository autowiredRepository;

    @PostConstruct
    private void init() {
        repository = this.autowiredRepository;
    }

    public static void loadData() {
        String path = "./input.json";

        try {
            if(repository.findAll().isEmpty()) {
                // create object mapper instance
                ObjectMapper mapper = new ObjectMapper();
                File resource = new FileSystemResource(path).getFile();
                // convert JSON file to map
                Map<String, Map<String, String>> map = mapper.readValue(resource, Map.class);

                // print map entries
                for (Map.Entry<String, Map<String, String>> memberShipMap : map.entrySet()) {

                    List<Member> members = new ArrayList<>();
                    for (Map.Entry<String, String> memberMap : memberShipMap.getValue().entrySet()) {
                        String id = UUID.randomUUID().toString();
                        Member m = new Member(id, memberMap.getKey(), memberMap.getValue());
                        members.add(m);
                    }
                    Membership membership = new Membership(memberShipMap.getKey(), members);
                    repository.save(membership);
                    System.out.println(memberShipMap.getKey() + "=" + memberShipMap.getValue());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
