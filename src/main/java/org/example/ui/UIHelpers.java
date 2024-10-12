package org.example.ui;

import org.example.manager.userManager.ProductoUserManager;
import org.example.manager.userManager.VentaMesaUserManager;
import org.example.model.Producto;
import org.example.ui.uiUser.UnifiedEditorRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionListener;

import static org.example.utils.Constants.*;


public class UIHelpers {

    private static ProductoUserManager productoUserManager = new ProductoUserManager();
    private static VentaMesaUserManager ventaMesaUserManager = new VentaMesaUserManager();

    private static JComboBox<String> productComboBox;
    private static JSpinner cantidadSpinner;
    private static JLabel totalLabel;
    private static JLabel totalCompraLabel;
    private static DefaultTableModel tableModel;
    private static Component compraDialog;

    public static JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);

        // Estilo del botón
        button.setFont(new Font("Arial", Font.BOLD, 18));  // Fuente y tamaño
        button.setFocusPainted(false);                     // Eliminar borde de foco
        button.setBackground(Color.WHITE);                 // Fondo blanco
        button.setForeground(Color.DARK_GRAY);             // Texto color gris oscuro
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // Márgenes del botón

        // Sombra para el botón
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Efecto de "levantarse" al pasar el mouse
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 230));  // Cambiar a gris claro al pasar el mouse
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                button.setBounds(button.getX(), button.getY() - 5, button.getWidth(), button.getHeight() + 5); // Levantar efecto
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);  // Volver al color blanco
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                button.setBounds(button.getX(), button.getY() + 5, button.getWidth(), button.getHeight() - 5); // Volver a la posición original
            }
        });

        // Establecer tamaño preferido del botón
        button.setPreferredSize(new Dimension(150, 100));  // Dimensiones personalizadas

        return button;
    }

    public static JTextField createTextField() {
        return new JTextField();
    }

    public static JDialog createDialog(String title, int width, int height, LayoutManager layout) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setSize(width, height);
        dialog.setLayout(layout);
        return dialog;
    }

    public static JPanel createInputPanel(JTable table, VentaMesaUserManager ventaMesaUserManager) {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2)); // Tres filas, dos columnas

        // Definir la fuente que se aplicará a todos los componentes
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font inputFont = new Font("Arial", Font.PLAIN, 14); // Para los campos de entrada

        // Primera fila: ComboBox de productos
        JLabel productLabel = new JLabel(PRODUCT_FIELD);
        productLabel.setFont(labelFont);
        inputPanel.add(productLabel);

        productComboBox = new JComboBox<>();
        productComboBox.setFont(new Font("Arial", Font.BOLD, 18)); // Cambiar la fuente del ComboBox
        java.util.List<Producto> productos = productoUserManager.getProducts();
        for (Producto producto : productos) {
            productComboBox.addItem(producto.getName());
        }
        inputPanel.add(productComboBox);

        // Segunda fila: Spinner de cantidad
        JLabel cantidadLabel = new JLabel(CANTIDAD_FIELD);
        cantidadLabel.setFont(labelFont);
        inputPanel.add(cantidadLabel);

        cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) cantidadSpinner.getEditor();
        spinnerEditor.getTextField().setFont(new Font("Arial", Font.BOLD, 18)); // Cambiar la fuente del Spinner
        inputPanel.add(cantidadSpinner);

        // Tercera fila: Espacio vacío para alineación y el botón alineado a la derecha
        inputPanel.add(new JLabel("")); // Espacio vacío en la primera celda de la fila

        // Crear un panel para el botón con FlowLayout alineado a la derecha
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton agregarProductoButton = createAddProductMesaButton(table, ventaMesaUserManager);
        agregarProductoButton.setFont(new Font("Arial", Font.BOLD, 20)); // Fuente del botón
        buttonPanel.add(agregarProductoButton);

        // Añadir el buttonPanel al inputPanel (segunda celda de la tercera fila)
        inputPanel.add(buttonPanel);

        return inputPanel;
    }

    public static JTable createProductTable() {
        String[] columnNames = {PRODUCTO, CANTIDAD, PRECIO_UNITARIO, TOTALP, SPACE}; // Sin título en la columna de eliminar
        tableModel = new DefaultTableModel(columnNames, ZERO);
        JTable table = new JTable(tableModel);
        UnifiedEditorRenderer editorRenderer = new UnifiedEditorRenderer(tableModel, ventaMesaUserManager);

        table.getColumnModel().getColumn(ONE); // Spinner en la columna de cantidad

        table.getColumnModel().getColumn(FOUR).setCellRenderer(editorRenderer);
        table.getColumnModel().getColumn(FOUR).setCellEditor(editorRenderer);
        return table;
    }

    public static JPanel createTotalPanel() {
        JPanel totalPanel = new JPanel(new GridLayout(THREE, ONE));
        totalLabel = new JLabel(TOTAL_DOUBLE_ZERO_INIT);
        totalCompraLabel = new JLabel(TOTAL_PURCHASE_INIT);
        

        totalPanel.add(totalLabel);
        totalPanel.add(totalCompraLabel);

        return totalPanel;
    }




    public static JButton createAddProductMesaButton(JTable table, VentaMesaUserManager ventaManager) {
        JButton agregarProductoButton = new JButton(AGREGAR_PRODUCTO);
        agregarProductoButton.addActionListener(e -> {
            try {
                String selectedProduct = (String) productComboBox.getSelectedItem();
                int cantidad = (int) cantidadSpinner.getValue();

                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Producto producto = productoUserManager.getProductByName(selectedProduct);

                // Verificar que el producto tiene suficiente stock
                if (producto.getCantidad() < cantidad) {
                    JOptionPane.showMessageDialog(null, "No hay suficiente stock para el producto: " + selectedProduct, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }



                // Añadir producto a la tabla
                double precioUnitario = producto.getPrice();
                double totalProducto = precioUnitario * cantidad;
                tableModel.addRow(new Object[]{selectedProduct, cantidad, precioUnitario, totalProducto, "X"});

                // Calcular el total general
                double total = 0;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    total += (double) tableModel.getValueAt(i, 3);  // Sumar la columna del total (index 3)
                }
                totalLabel.setText(TOTAL_PESO + total);
                totalCompraLabel.setText(TOTAL_COMPRA_PESO + total);

                // Añadir el producto al carrito de ventas
                productoUserManager.addProductToCart(producto, cantidad);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(compraDialog, INVALID_AMOUNT, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            }
        });

        return agregarProductoButton;
    }

}
