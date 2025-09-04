package org.example.Dao;

import org.example.connection.OracleDbConnection;
import org.example.entity.AssetsHeld;
import org.example.entity.User;

import java.sql.SQLException;

public interface UserDao {
    public void addUser(User user) throws SQLException;
    public void purchaseAsset(AssetsHeld assetsHeld) throws SQLException;
}
