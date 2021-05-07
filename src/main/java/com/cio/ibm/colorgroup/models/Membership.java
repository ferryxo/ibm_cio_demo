package com.cio.ibm.colorgroup.models;

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Membership {

    @Id
    private String id;

    private String group;

    private List<Member> member;

    public Membership() {
        member = new ArrayList<>();
    }

    public Membership(String key, List<Member> members) {
        this();
        this.group = key;
        member = members;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Member> getMember() {
        return member;
    }

    public void setMember(List<Member> member) {
        this.member = member;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getId().equals(((Membership)obj).getId());
    }
}
