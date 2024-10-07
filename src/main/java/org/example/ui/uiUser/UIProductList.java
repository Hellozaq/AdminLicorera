package org.example.ui.uiUser;

import org.example.manager.userManager.ProductoManager;
import org.example.model.Producto;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static org.example.ui.uiUser.UIHelpers.createButton;
import static org.example.ui.uiUser.UIHelpers.createDialog;
import static org.example.utils.Constants.CLOSE_BUTTON;
import static org.example.utils.Constants.LISTAR_PRODUCTO;

public class UIProductList {
    private static ProductoManager productoManager = new ProductoManager();


    public static void showListProductsDialog() {
        // Crear el diálogo
        JDialog listProductsDialog = createDialog(LISTAR_PRODUCTO, 1000, 600, new BorderLayout());
        listProductsDialog.setResizable(true); // Permitir que la ventana sea redimensionable

        // Obtener la lista de productos
        List<Producto> products = productoManager.getProducts();
        String[] columnNames = {"Nombre", "Cantidad", "Precio"};
        Object[][] data = new Object[products.size()][3];

        // Llenar los datos en la matriz
        for (int i = 0; i < products.size(); i++) {
            NumberFormat formatCOP = NumberFormat.getInstance(new Locale("es", "CO"));
            Producto p = products.get(i);
            double precio = p.getPrice();
            data[i][0] = p.getName(); // Nombre
            data[i][1] = p.getQuantity(); // Cantidad
            data[i][2] = formatCOP.format(precio);
        }

        // Crear el JTable
        JTable productTable = new JTable(data, columnNames);
        productTable.setFillsViewportHeight(true);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Ajustar automáticamente el tamaño de las columnas

        // Establecer la fuente y el tamaño
        Font font = new Font("Arial", Font.PLAIN, 18); // Cambiar el tipo y tamaño de fuente
        productTable.setFont(font);
        productTable.setRowHeight(30); // Aumentar la altura de las filas

        // Establecer la fuente para el encabezado
        JTableHeader header = productTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20)); // Fuente más grande para el encabezado
        header.setBackground(Color.LIGHT_GRAY); // Fondo para el encabezado
        header.setForeground(Color.BLACK); // Color del texto del encabezado

        // Configuración de borde para mejorar la visibilidad
        productTable.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        productTable.setBackground(Color.WHITE); // Fondo de la tabla
        productTable.setSelectionBackground(Color.CYAN); // Color de selección
        productTable.setSelectionForeground(Color.BLACK); // Color del texto seleccionado

        // Añadir el JTable dentro de un JScrollPane
        JScrollPane scrollPane = new JScrollPane(productTable);
        listProductsDialog.add(scrollPane, BorderLayout.CENTER);

        // Botón de cerrar
        JButton closeButton = createButton(CLOSE_BUTTON, e -> listProductsDialog.dispose());
        closeButton.setFont(new Font("Arial", Font.BOLD, 18)); // Aumentar la fuente del botón
        listProductsDialog.add(closeButton, BorderLayout.SOUTH);

        // Mostrar el diálogo
        listProductsDialog.setVisible(true);
        listProductsDialog.setLocationRelativeTo(null);
    }
}
