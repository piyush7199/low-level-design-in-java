package org.lld.practice.design_library_management_system.improved_solution.repository;

import org.lld.practice.design_library_management_system.improved_solution.models.Member;

import java.util.HashMap;
import java.util.Map;

public class MemberRegistry {
    private final Map<String, Member> members;

    public MemberRegistry() {
        this.members = new HashMap<>();
    }

    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
        System.out.println("Member registered: " + member.getName() + " (" + member.getMemberType() + ")");
    }

    public Member getMember(String memberId) {
        return members.get(memberId);
    }

    public boolean isMemberActive(String memberId) {
        Member member = members.get(memberId);
        return member != null && member.isActive();
    }
}

