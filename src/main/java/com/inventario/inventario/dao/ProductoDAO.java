package com.inventario.inventario.dao;

import org.springframework.data.repository.CrudRepository;

import com.inventario.inventario.modelo.Producto;

public interface ProductoDAO extends CrudRepository<Producto, Long>{       
    
}
