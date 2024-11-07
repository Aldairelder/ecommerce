package com.curso.ecommerce.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Lob
    private String descripcion;

    private String imagen;

    @Column(name = "precio_compra", nullable = false)
    private double precioCompra;  // Precio de compra

    @Column(name = "precio_venta", nullable = false)
    private double precioVenta;   // Precio de venta

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = true)
    private categoria categoria;   // Relación con la tabla de categorías

    private int cantidad;             // Inventario del producto

    private double descuento;      // Descuento sobre el precio de venta

    private String marca;          // Marca del producto

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;        // Fecha de creación

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", insertable = false)
    private Date updatedAt;        // Fecha de actualización

    // Constructor vacío
    public Producto() {
    }

    // Constructor con parámetros
    public Producto(Integer id, String nombre, String descripcion, String imagen, double precioCompra,
                    double precioVenta, categoria categoria, int cantidad, double descuento, String marca,
                    Date createdAt, Date updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.marca = marca;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(categoria categoria) {
        this.categoria = categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", imagen=" + imagen
                + ", precioCompra=" + precioCompra + ", precioVenta=" + precioVenta + ", categoria=" + categoria
                + ", cantidad=" + cantidad + ", descuento=" + descuento + ", marca=" + marca + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + "]";
    }
}
