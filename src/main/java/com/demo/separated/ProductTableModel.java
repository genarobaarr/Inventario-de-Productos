package com.demo.separated;

import com.demo.connection.DatabaseConnection;
import com.demo.dao.ProductDAO;
import com.demo.dao.ProductDAOImp;
import com.demo.model.Login;
import com.demo.model.Product;

import javax.swing.table.AbstractTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {
    String tableDBName = "productos";
    private final String[] columnNames = {"ID", "Nombre", "Precio"};
    private final List<Product> productos;

    public ProductTableModel() {
        this.productos = new ArrayList<>();
    }

    public ProductTableModel(List<Product> productos) {

        this.productos = productos;
    }

    @Override
    public int getRowCount() {
        return productos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product producto = productos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return producto.getId();
            case 1:
                return producto.getNombre();
            case 2:
                return producto.getPrecio();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex >= 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Product producto = productos.get(rowIndex);
        switch (columnIndex) {
            case 0:
                producto.setId(aValue.toString());
                break;
            case 1:
                producto.setNombre(aValue.toString());
                break;
            case 2:
                producto.setPrecio(Double.parseDouble(aValue.toString()));
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addProducto(Product producto) throws SQLException {
        ProductDAO productDAO = new ProductDAOImp();
        List<Product> productos2 = productDAO.readAll();

        for (Product producto2 : productos2) {

            if (producto2.getId().equals(producto.getId())) {
                throw new SQLException("El ID ya existe!");
            }
        }

        Connection conn = DatabaseConnection.getInstance().getConnection();
        String insertQuery = "INSERT INTO " +tableDBName+ " (id, nombre, precio) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(insertQuery);
        ps.setString(1, producto.getId());
        ps.setString(2, producto.getNombre());
        ps.setDouble(3, producto.getPrecio());
        ps.execute();

        productos.add(producto);
        fireTableRowsInserted(productos.size() - 1, productos.size() - 1);
    }

    public void removeProducto(int rowIndex) throws  SQLException{

        Product producto = productos.get(rowIndex);

        Connection conn = DatabaseConnection.getInstance().getConnection();
        String query = "DELETE FROM " +tableDBName+ " WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, producto.getId());
        ps.executeUpdate();

        productos.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Product getProductoAt(int rowIndex) {
        return productos.get(rowIndex);
    }

    public List<Product> getProductos() {
        return productos;
    }
}