package com.hrhelpdesk.model;

public class Admin extends User {
    public Admin() {}

    @Override
    public boolean canSubmitTicket() { return true; }
    @Override
    public boolean canViewAllTickets() { return true; }
}