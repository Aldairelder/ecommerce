package com.curso.ecommerce.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "categorias")
public class categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;  // Fecha de creación

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", insertable = false)
    private Date updatedAt;  // Fecha de actualización

    // Constructor vacío
    public categoria() {
    }

    // Constructor con parámetros
    public categoria(Integer id, String nombre, Date createdAt, Date updatedAt) {
        this.id = id;
        this.nombre = nombre;
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
        return "Categoria [id=" + id + ", nombre=" + nombre + 
               ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
