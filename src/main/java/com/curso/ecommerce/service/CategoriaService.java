package com.curso.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.categoria;
import com.curso.ecommerce.repository.CategoriaRepository;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<categoria> findAll() {
        return categoriaRepository.findAll();
    }
    public categoria findById(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }
}

