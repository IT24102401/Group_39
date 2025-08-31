package com.hrhelpdesk;

import org.mindrot.jbcrypt.BCrypt;

public class HashGenerator {
    public static void main(String[] args) {
        String adminPassword = "admin123";
        String employeePassword = "pass123";

        String adminHash = BCrypt.hashpw(adminPassword, BCrypt.gensalt(10)); // Work factor 10
        String employeeHash = BCrypt.hashpw(employeePassword, BCrypt.gensalt(10));

        System.out.println("Admin Hash: " + adminHash);
        System.out.println("Employee Hash: " + employeeHash);
    }
}