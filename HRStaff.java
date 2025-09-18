package com.hrhelpdesk.model;

public class HRStaff extends User {
    public HRStaff() {}

    @Override
    public boolean canSubmitTicket() { return true; }
    @Override
    public boolean canViewAllTickets() { return true; }
}