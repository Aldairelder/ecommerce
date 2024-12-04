package com.curso.ecommerce.service;

import com.curso.ecommerce.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    Optional<Usuario> findByEmail(String email); // Método existente

    // Métodos CRUD adicionales
    List<Usuario> findAll();
    Optional<Usuario> getById(Integer id);
    Usuario save(Usuario usuario);
    void update(Usuario usuario);
    void delete(Integer id);
}
