package com.demo.dao;

import com.demo.connection.DatabaseConnection;
import com.demo.model.Login;
import com.demo.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImp implements ProductDAO {
    private String tableName;

    public ProductDAOImp(){
        tableName = "Productos";
    }
    @Override
    public void create(Product product) throws SQLException {

    }

    @Override
    public Login read(int id) throws SQLException {
        return null;
    }

    @Override
    public void update(Product product) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public List<Product> readAll() throws SQLException {
        List<Product> products = new ArrayList();
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT id, nombre, precio FROM "+tableName;
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            products.add(new Product(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDouble(3)
            ));
        }
        return products;
    }
}
