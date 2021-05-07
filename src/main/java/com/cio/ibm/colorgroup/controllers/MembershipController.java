package com.cio.ibm.colorgroup.controllers;

import com.cio.ibm.colorgroup.dao.MembershipRepository;
import com.cio.ibm.colorgroup.models.Member;
import com.cio.ibm.colorgroup.models.Membership;
import liquibase.pro.packaged.m;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(path = "${apiversion}/")
public class MembershipController {

    @Autowired
    MembershipRepository membershipRepository;

    @GetMapping(value="healthcheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> healthCheck(AuthenticationPrincipal auth){
        Map<String,String> m = new HashMap<>();
        m.put("status", "ok");
        return m;
    }

    @GetMapping(value="members/colors/{color}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Membership> getPeopleByColor(@PathVariable String color){
        return membershipRepository.findPeopleByColor(color);
    }

    @GetMapping(value="members/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Membership> getGroupByPerson(@PathVariable String id){
        return membershipRepository.findGroupByPersonId(id);
    }

    @GetMapping(value="members/names/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Membership> getGroupByPersonName(@PathVariable String name){
        return membershipRepository.findGroupByPersonName(name);
    }

    @GetMapping(value="members/{name}/color", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getColorByPerson(@PathVariable String name){
        String color = membershipRepository.findColorByPerson(name);
        return Map.of("Color", color);
    }

    @GetMapping(value="colors", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<String> getColors(){
        return membershipRepository.findAllColors();
    }

    @GetMapping(value="groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Membership> getAllGroups(){
        return membershipRepository.findAll();
    }

    @PostMapping(value="members/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> editMember(@PathVariable String memberId, @RequestBody Membership editedMembership) {
        try {

            for (Member memberToEdit : editedMembership.getMember()) {
                Optional<Membership> originOpt = editedMembership.getId() != null
                        ? membershipRepository.findById(editedMembership.getId())
                        : Optional.empty();
                List<Membership> dest = membershipRepository.findByGroup(editedMembership.getGroup());

                if (originOpt.isPresent()) {
                    Membership originGroup = originOpt.get();
                    //remove original first, add edited version below
                    originGroup.getMember().removeIf(m -> m.getId().equals(memberId));
                    //user move group, update its origin & dest
                    if (!originGroup.getGroup().equals(editedMembership.getGroup())) {
                        if (!dest.isEmpty()) {
                            Membership destGroup = dest.get(0);
                            destGroup.getMember().add(memberToEdit);
                            membershipRepository.save(destGroup);
                        } else {
                            //user move to a new group, get a new id
                            editedMembership.setId(null);
                            membershipRepository.save(editedMembership);
                        }
                    } else {
                        originGroup.getMember().add(memberToEdit);
                    }
                    membershipRepository.save(originGroup);
                }
                else {
                    //add use case
                    memberToEdit.setId(UUID.randomUUID().toString());
                    if(dest.isEmpty()) {
                        membershipRepository.save(editedMembership);
                    } else {
                        Membership destGroup = dest.get(0);
                        destGroup.getMember().add(memberToEdit);
                        membershipRepository.save(destGroup);
                    }
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("{'status': 'ok'}");
    }

    @DeleteMapping(value="members/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteMember(@PathVariable String memberId) {
        try {
            List<Membership> memberships = membershipRepository.findGroupByPersonId(memberId);
            if(!memberships.isEmpty()) {
                for (Membership membership : memberships) {
                    membership.getMember().removeIf(m -> m.getId().equals(memberId));
                    membershipRepository.save(membership);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("{'status': 'ok'}");
    }

    @GetMapping(value="members/names", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<String> getAllPeople(){
        return membershipRepository.findAllPeople();
    }

    @GetMapping(value="groups/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Membership> getGroups(@PathVariable String id){
        return membershipRepository.findByGroup(id);
    }

    @GetMapping(value="groups/{id}/members/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Membership> getGroupByIdAndName(@PathVariable String id, @PathVariable String name){
        return membershipRepository.findPeopleByGroupAndName(id, name);
    }

    @GetMapping(value="groups/{id}/colors/{color}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Membership> getGroupByIdAndColor(@PathVariable String id, @PathVariable String color){
        return membershipRepository.findPeopleByGroupAndColor(id, color);
    }

    @GetMapping(value="groups/names", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<String> getGroups(){
        return membershipRepository.findGroupNames();
    }
}
