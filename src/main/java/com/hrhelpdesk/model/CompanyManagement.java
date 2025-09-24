package com.hrhelpdesk.model;

public class CompanyManagement extends User {
    public CompanyManagement() {}

    @Override
    public boolean canSubmitTicket() { return false; }
    @Override
    public boolean canViewAllTickets() { return true; }
}