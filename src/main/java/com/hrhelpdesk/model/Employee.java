package com.hrhelpdesk.model;

public class Employee extends User {
    public Employee() {}

    @Override
    public boolean canSubmitTicket() { return true; } // Employees can create/cancel tickets, attachments
    @Override
    public boolean canViewAllTickets() { return false; } // Only own tickets
}