package org.example.Dao;

import org.example.entity.Liability;

import java.sql.SQLException;

public interface LiabilityDao {
    public void addLiability(Liability liability) throws SQLException;
}
