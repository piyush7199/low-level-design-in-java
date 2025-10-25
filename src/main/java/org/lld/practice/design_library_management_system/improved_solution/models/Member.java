package org.lld.practice.design_library_management_system.improved_solution.models;

public class Member {
    private final String memberId;
    private final String name;
    private final String email;
    private final MemberType memberType;
    private MemberStatus status;
    private double totalFines;

    public Member(String memberId, String name, String email, MemberType memberType) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.memberType = memberType;
        this.status = MemberStatus.ACTIVE;
        this.totalFines = 0.0;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public double getTotalFines() {
        return totalFines;
    }

    public void addFine(double amount) {
        this.totalFines += amount;
    }

    public void payFine(double amount) {
        this.totalFines = Math.max(0, this.totalFines - amount);
    }

    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }

    public int getMaxBooksAllowed() {
        return memberType.getMaxBooks();
    }

    public int getMaxBorrowDays() {
        return memberType.getMaxDays();
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + memberType +
                ", status=" + status +
                '}';
    }
}

