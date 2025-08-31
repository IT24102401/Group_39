package com.hrhelpdesk.dao;


import com.hrhelpdesk.db.DBConnection;
import com.hrhelpdesk.model.TicketCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketCategoryDAO {
    public List<TicketCategory> getAllCategories() throws SQLException {
        List<TicketCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM TICKET_CATEGORY";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TicketCategory category = new TicketCategory();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setIsaTableName(rs.getString("isa_table_name"));
                categories.add(category);
            }
        }
        return categories;
    }

    public String getIsaById(int categoryId) throws SQLException {
        String sql = "SELECT isa_table_name FROM TICKET_CATEGORY WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("isa_table_name");
                }
            }
        }
        return null;
    }
}