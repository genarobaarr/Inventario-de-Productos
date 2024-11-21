package com.demo.gui;

import com.demo.connection.DatabaseConnection;
import com.demo.model.Product;
import com.demo.separated.ProductTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ProductApp extends JFrame {

    String tableName = "productos";
    private String loggedUser;
    private ProductTableModel tableModel;
    private JTable table;
    JPanel inputPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductApp productApp = new ProductApp("Administrador");
            productApp.setVisible(true);
        });
    }

    public ProductApp(String loggedUser){

        this.loggedUser = loggedUser;
        setTitle("Productos - Logeado como: " + loggedUser);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());

        init();
        loadInitialProducts();

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }

    private void init() {

        tableModel = new ProductTableModel();
        table = new JTable(tableModel);

        // Panel superior para las entradas
        inputPanel = new JPanel(new FlowLayout());
        JTextField txtId = new JTextField(5);
        JTextField txtNombre = new JTextField(15);
        JTextField txtPrecio = new JTextField(10);

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(txtId);
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(txtNombre);
        inputPanel.add(new JLabel("Precio:"));
        inputPanel.add(txtPrecio);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        inputPanel.add(btnAgregar);
        inputPanel.add(btnEliminar);


        btnAgregar.addActionListener(e -> {
                try {
                    String id = txtId.getText();
                    String nombre = txtNombre.getText();
                    String precio = txtPrecio.getText();

                    if (id.isEmpty() || nombre.isEmpty() || precio.isEmpty()) {
                        JOptionPane.showMessageDialog(inputPanel, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        tableModel.addProducto(new Product(id, nombre, Double.parseDouble(precio)));
                        txtId.setText("");
                        txtNombre.setText("");
                        txtPrecio.setText("");
                    }
                } catch (SQLException d) {
                    throw new RuntimeException(d);
                }

        });

        btnEliminar.addActionListener(e -> {
                try {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        tableModel.removeProducto(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(inputPanel, "Seleccione una fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException d) {
                    throw new RuntimeException (d);
                }

        });
    }

    public void loadInitialProducts() {
        try {
            String tableDBName = "productos";
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT id, nombre, precio FROM " + tableDBName;
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                tableModel.getProductos().add(new Product(id, nombre, precio));
            }

            tableModel.fireTableDataChanged();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
