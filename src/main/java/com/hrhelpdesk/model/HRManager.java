package com.hrhelpdesk.model;

public class HRManager extends User {
    public HRManager() {}

    @Override
    public boolean canSubmitTicket() { return true; }
    @Override
    public boolean canViewAllTickets() { return true; }
}