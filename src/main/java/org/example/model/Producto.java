package org.example.model;

import java.util.List;

public class Producto {
    private static int nextId = 1; // Contador estático para el ID auto-incremental

    private int id;
    private String name;
    private int quantity ;
    private double price;
    private String foto;

    public Producto(int id,String name, int quantity, double price, String foto) {

        this.id = id; // Asigna el ID actual y lo incrementa
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.foto = foto;
    }

    public static String getFotoByName(String nombre, List<Producto> productos) {
        for (Producto p : productos) {
            if (p.getName().equalsIgnoreCase(nombre)) {
                return p.getFoto();
            }
        }
        return "sin_imagen.png"; // Imagen por defecto si no se encuentra
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }


    public void setCantidad(int cantidad) {
        this.quantity = cantidad;
    }

    // Implementamos equals para comparar productos por nombre
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto producto = (Producto) obj;
        return name.equalsIgnoreCase(producto.name); // Comparación basada en nombre (ignorar mayúsculas/minúsculas)
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    public String getFoto() {
        return foto;
    }

    public void setPrecio(double precio) {
        this.price = precio;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int abs) {
        this.id = abs;
    }
}
