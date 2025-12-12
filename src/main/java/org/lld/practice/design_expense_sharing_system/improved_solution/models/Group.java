package org.lld.practice.design_expense_sharing_system.improved_solution.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a group of users sharing expenses.
 */
public class Group {
    private final String groupId;
    private final String name;
    private final String createdBy;
    private final Set<String> memberIds;
    
    public Group(String name, String createdBy) {
        this.groupId = UUID.randomUUID().toString();
        this.name = name;
        this.createdBy = createdBy;
        this.memberIds = new HashSet<>();
        this.memberIds.add(createdBy);
    }
    
    public String getGroupId() {
        return groupId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public Set<String> getMemberIds() {
        return new HashSet<>(memberIds);
    }
    
    public void addMember(String userId) {
        memberIds.add(userId);
    }
    
    public void removeMember(String userId) {
        memberIds.remove(userId);
    }
    
    public boolean hasMember(String userId) {
        return memberIds.contains(userId);
    }
}

