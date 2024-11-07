package com.curso.ecommerce.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detalles")
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private double cantidad;

    @ManyToOne
    private Orden orden;
    
    @ManyToOne
    private Producto producto;

    public DetalleOrden() {
    }

    public DetalleOrden(Integer id, String nombre, double cantidad, Producto producto) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // Método dinámico para calcular el total usando el precioVenta del producto
    public double getTotal() {
        return this.producto.getPrecioVenta() * this.cantidad; // Aquí usamos precioVenta
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return "DetalleOrden [id=" + id + ", nombre=" + nombre + ", cantidad=" + cantidad + ", precio=" + producto.getPrecioVenta()
                + ", total=" + getTotal() + "]"; // Cambiado para usar el método getTotal()
    }
}
