package com.demo.dao;

import com.demo.model.Login;
import com.demo.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {

    public void create(Product product) throws SQLException;
    public Login read(int id) throws SQLException;
    public void update(Product product) throws SQLException;
    public void delete(int id) throws SQLException;
    public List<Product> readAll() throws SQLException;

}
