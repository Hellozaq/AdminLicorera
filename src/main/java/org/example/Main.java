package org.example;

import org.example.manager.FacturacionManager;
import org.example.manager.VentaManager;
import org.example.manager.ProductoManager;
import org.example.model.Producto;
import org.example.ui.UIHelpers;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import static org.example.ui.UIHelpers.*;
import static org.example.utils.Constants.*;

public class Main {
    private static ProductoManager productoManager = new ProductoManager();


    private static JDialog ventaDialog;
// eliminar botones

    public static void main(String[] args) {
        crearDirectorios();
        JFrame frame = new JFrame(CALCULADORA_ADMINISTRADOR);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Cambiar a DO_NOTHING_ON_CLOSE
        frame.setUndecorated(true); // Quitar los decorados de la ventana
        frame.setSize(FOUR_HUNDRED, FOUR_HUNDRED);
        frame.setLayout(new GridLayout(THREE, ONE));

        FacturacionManager facturacionManager = new FacturacionManager(); // Instancia de FacturacionManager

        // Botones principales
        JButton ventaButton = UIHelpers.createButton(VENTA, e -> showVentaDialog());
        JButton adminProductosButton = UIHelpers.createButton(ADMINISTRAR_PRODUCTOS, e -> showAdminProductosDialog());
        JButton salirButton = UIHelpers.createButton(SALIR_FACTURAR, e -> {
            // Mostrar un cuadro de diálogo solicitando que el usuario escriba "Facturar"
            String input = JOptionPane.showInputDialog(null, POR_FAVOR_ESCRIBE_FACTURAR, CONFIRMAR_FACTURACION, JOptionPane.QUESTION_MESSAGE);

            // Verificar si el usuario ingresó correctamente la palabra "Facturar"
            if (facturacionManager.verificarFacturacion(input)) {
                // Si la palabra es correcta, se procede a facturar y salir
                facturacionManager.facturarYSalir();

            } else {
                // Si la palabra es incorrecta o el usuario cancela, mostrar un mensaje y regresar al menú principal
                facturacionManager.mostrarErrorFacturacion();
            }
        });

        frame.add(ventaButton);
        frame.add(adminProductosButton);
        frame.add(salirButton);

        // Centrar el frame en la pantalla
        frame.setLocationRelativeTo(null);

        // Mostrar la ventana principal
        frame.setVisible(true);
    }

    private static void showListProductsDialog() {
        // Crear el diálogo
        JDialog listProductsDialog = UIHelpers.createDialog(LISTAR_PRODUCTO, FOUR_HUNDRED, THREE_HUNDRED, new BorderLayout());

        // Crear el área de texto
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        // Obtener la lista de productos y formatearla
        List<Producto> products = productoManager.getProducts();
        String formattedProductList = productoManager.formatProductList(products);

        // Asignar la lista formateada al área de texto
        textArea.setText(formattedProductList);

        // Añadir el área de texto dentro de un JScrollPane
        listProductsDialog.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Botón de cerrar
        JButton closeButton = UIHelpers.createButton(CLOSE_BUTTON, e -> listProductsDialog.dispose());
        listProductsDialog.add(closeButton, BorderLayout.SOUTH);

        // Mostrar el diálogo
        listProductsDialog.setVisible(true);
        listProductsDialog.setLocationRelativeTo(null);

    }

    private static void showAdminProductosDialog() {
        JDialog adminDialog = UIHelpers.createDialog( ADMINISTRAR_PRODUCTOS , THREE_HUNDRED, TWO_HUNDRED, new GridLayout(2, 1));

        // Botones
        JButton addButton = UIHelpers.createButton(AGREGAR_PRODUCTO, e -> showAddProductDialog());
        JButton listButton = UIHelpers.createButton(LISTAR_PRODUCTO, e -> showListProductsDialog());

        // Añadir botones al diálogo
        adminDialog.add(addButton);
        adminDialog.add(listButton);
        adminDialog.setLocationRelativeTo(null);
        adminDialog.setVisible(true);
    }

    private static void showAddProductDialog() {
        ProductoManager productoManager = new ProductoManager();
        JDialog addProductDialog = UIHelpers.createDialog(AGREGAR_PRODUCTO, THREE_HUNDRED, TWO_HUNDRED, new GridLayout(3, 2));

        // Crear los campos de entrada
        JTextField nameField = productoManager.createField(addProductDialog, PRODUCTO_FIELD_NOMBRE);
        /*JTextField quantityField = productoManager.createField(addProductDialog, PRODUCTO_FIELD_CANTIDAD);*/
        JTextField priceField = productoManager.createField(addProductDialog, PRODUCTO_FIELD_PRECIO);

        // Botón para agregar el producto
        JButton addButton = UIHelpers.createButton(AGREGAR_BTN, e -> {
            productoManager.addProductFromFields( nameField,/* quantityField,*/ priceField, addProductDialog);
        });

        addProductDialog.add(addButton);
        addProductDialog.setVisible(true);
        addProductDialog.setLocationRelativeTo(null);
    }


    public static void showVentaDialog() {
        ventaDialog = UIHelpers.createDialog(REALIZAR_VENTA, FIVE_HUNDRED, FOUR_HUNDRED, new BorderLayout());

        JPanel inputPanel = createInputPanel();
        ventaDialog.add(inputPanel, BorderLayout.NORTH);

        JTable table = createProductTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        ventaDialog.add(tableScrollPane, BorderLayout.CENTER);

        JPanel totalPanel = createTotalPanel();
        ventaDialog.add(totalPanel, BorderLayout.SOUTH);

        VentaManager ventaManager = new VentaManager();

        JPanel buttonPanel = createButtonPanel(table, ventaManager, ventaDialog);
        ventaDialog.add(buttonPanel, BorderLayout.SOUTH);

        ventaDialog.setVisible(true);
        ventaDialog.setLocationRelativeTo(null);

    }
    public static void crearDirectorios() {
        String documentosPath = System.getProperty("user.home") + "\\Documents\\Calculadora del Administrador";

        String facturasPath = documentosPath + "\\Facturas";
        String realizadoPath = documentosPath + "\\Realizo";

        crearDirectorioSiNoExiste(facturasPath);
        crearDirectorioSiNoExiste(realizadoPath);
    }

    private static void crearDirectorioSiNoExiste(String path) {
        File directorio = new File(path);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio creado: " + path);
            } else {
                System.out.println("No se pudo crear el directorio: " + path);
            }
        } else {
            System.out.println("El directorio ya existe: " + path);
        }
    }
/*private static void showMesas() {
        // Lógica para el manejo de mesas
    }*/
}
