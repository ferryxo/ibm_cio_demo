package com.cio.ibm.colorgroup.controllers;

import com.cio.ibm.colorgroup.dao.MembershipRepository;
import com.cio.ibm.colorgroup.models.Membership;
import com.cio.ibm.colorgroup.models.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MembershipController.class)
class MembershipControllerTest {

    private static final String API_V_1 = "/api/v1/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MembershipRepository membershipRepository;

    private List<Membership> membershipList;
    private Membership membership;

    @BeforeEach
    void setUp() {
        List<Member> memberMap = new ArrayList<>();
        Member m = new Member("1", "TestPerson", "TestColor");
        memberMap.add(m);
        membership = new Membership("TestGroup", memberMap);
        membershipList = Collections.singletonList(membership);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getPeopleByColor() throws Exception {

        when(membershipRepository.findPeopleByColor(anyString())).thenReturn(membershipList);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(membershipList);

        this.mockMvc.perform(get(API_V_1 + "members/colors/Red"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));

    }

    @Test
    void getGroupByPerson() throws Exception {

        when(membershipRepository.findGroupByPersonName(anyString())).thenReturn(membershipList);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(membershipList);

        this.mockMvc.perform(get(API_V_1 + "members/Tim"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @Test
    void getColorByPerson() throws Exception {
        String color = "Red";
        when(membershipRepository.findColorByPerson(anyString())).thenReturn(color);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(Map.of("Color", color));

        this.mockMvc.perform(get(API_V_1 + "members/Nguyen/color"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @Test
    void getColors() throws Exception {
        Set<String> colors = new HashSet<>();
        colors.add("Red");
        when(membershipRepository.findAllColors()).thenReturn(colors);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(colors);

        this.mockMvc.perform(get(API_V_1 + "colors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @Test
    void getAllGroups() throws Exception {
        when(membershipRepository.findAll()).thenReturn(membershipList);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(membershipList);

        this.mockMvc.perform(get(API_V_1 + "groups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @Test
    void editMembership() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(membership);

        RequestBuilder request = MockMvcRequestBuilders
                .post(API_V_1 + "members/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'status': 'ok'}"))
                .andReturn();

    }

    @Test
    void getAllPeople() throws Exception {
        Set<String> people = new HashSet<>();
        people.add("Tim");
        when(membershipRepository.findAllPeople()).thenReturn(people);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(people);

        this.mockMvc.perform(get(API_V_1 + "members/names"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));

    }

    @Test
    void getGroups() throws Exception {
        Set<String> groupNames = new HashSet<>();
        groupNames.add("Group1");
        when(membershipRepository.findGroupNames()).thenReturn(groupNames);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(groupNames);

        this.mockMvc.perform(get(API_V_1 + "groups/names"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }


    @Test
    void getGroupByIdAndName() throws Exception {
        when(membershipRepository.findPeopleByGroupAndName(anyString(), anyString())).thenReturn(membershipList);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(membershipList);

        this.mockMvc.perform(get(API_V_1 + "groups/Group2/members/Jane"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }

    @Test
    void getGroupByIdAndColor() throws Exception {
        when(membershipRepository.findPeopleByGroupAndColor(anyString(), anyString())).thenReturn(membershipList);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(membershipList);

        this.mockMvc.perform(get(API_V_1 + "groups/Group2/colors/Red"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonString));
    }
}