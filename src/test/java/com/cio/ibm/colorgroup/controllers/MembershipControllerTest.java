package com.cio.ibm.colorgroup.controllers;

import com.cio.ibm.colorgroup.dao.MembershipRepository;
import com.cio.ibm.colorgroup.models.Membership;
import com.cio.ibm.colorgroup.models.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    void setUp() {
        List<Member> memberList = new ArrayList<>();
        Member m = new Member("1", "TestPerson", "TestColor");
        memberList.add(m);
        membership = new Membership("TestGroup", memberList);
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

    @Test
    void editMemberAddToNewGroup() throws Exception {
        List<Member> memberList = new ArrayList<>();
        final String memberId = "1";

        Member m = new Member(memberId, "Person1", "Red");
        memberList.add(m);
        Membership originalMembership = new Membership("Group1", memberList);
        Membership editRequest = new Membership("Group2", memberList);
        editRequest.setId("1");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(editRequest);

        //origin group
        when(membershipRepository.findById(anyString())).thenReturn(Optional.of(originalMembership));
        //add to a new group
        when(membershipRepository.findByGroup(anyString())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(API_V_1 + "members/" + memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Membership> argument = ArgumentCaptor.forClass(Membership.class);

        verify(membershipRepository, times(2)).save(argument.capture());
        List<Membership> argVals = argument.getAllValues();
        assertEquals(2, argVals.size());
        assertNull(argVals.get(0).getId());
        assertEquals(1, argVals.get(0).getMember().size());
        //remove member from the original group
        assertEquals(0, argVals.get(1).getMember().size());
    }

    @Test
    void editMember_MoveToExistingGroup_UpdateOriginAndDestinationGroups() throws Exception {
        List<Member> memberList = new ArrayList<>();
        final String memberId = "1";

        Member m = new Member(memberId, "Person1", "Red");
        memberList.add(m);
        Membership originalMembership = new Membership("Group1", memberList);
        Membership editRequest = new Membership("Group2", memberList);
        editRequest.setId("1");

        List<Member> destMemberList = new ArrayList<>();
        Member destCurrentMember = new Member(memberId, "Person2", "Red");
        destMemberList.add(destCurrentMember);
        Membership destMembership = new Membership("Group2", destMemberList);
        destMembership.setId("2");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(editRequest);

        //origin group
        when(membershipRepository.findById(anyString())).thenReturn(Optional.of(originalMembership));
        //add to an existing group
        when(membershipRepository.findByGroup(anyString())).thenReturn(Collections.singletonList(destMembership));

        this.mockMvc.perform(post(API_V_1 + "members/" + memberId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Membership> argument = ArgumentCaptor.forClass(Membership.class);

        verify(membershipRepository, times(2)).save(argument.capture());
        List<Membership> argVals = argument.getAllValues();
        assertEquals(2, argVals.size());
        assertEquals("2", argVals.get(0).getId());
        assertEquals(2, argVals.get(0).getMember().size());
        //remove member from the original group
        assertEquals(0, argVals.get(1).getMember().size());
    }

    @Test
    void deleteMemberFromAGroup_PreserveOtherMembers() throws Exception {
        List<Member> memberList = new ArrayList<>();

        Member m = new Member("1", "Person1", "Red");
        Member m2 = new Member("2", "Person2", "Red");
        memberList.add(m);
        memberList.add(m2);
        Membership originalMembership = new Membership("Group1", memberList);
        List<Membership> mshipList = new ArrayList<>();
        mshipList.add(originalMembership);

        when(membershipRepository.findGroupByPersonId(anyString())).thenReturn(mshipList);

        this.mockMvc.perform(delete(API_V_1 + "members/1"))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Membership> argument = ArgumentCaptor.forClass(Membership.class);

        verify(membershipRepository, times(1)).save(argument.capture());
        Membership argVal = argument.getValue();
        assertEquals(1, argVal.getMember().size());
        //remove member from the original group
        assertEquals(m2, argVal.getMember().get(0));
    }
}