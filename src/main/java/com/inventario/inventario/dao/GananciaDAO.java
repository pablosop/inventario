package com.inventario.inventario.dao;

import org.springframework.data.repository.CrudRepository;

import com.inventario.inventario.modelo.Ganancia;

public interface GananciaDAO extends CrudRepository<Ganancia, Long>{
    
}
