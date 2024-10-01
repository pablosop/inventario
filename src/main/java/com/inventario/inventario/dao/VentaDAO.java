package com.inventario.inventario.dao;

import org.springframework.data.repository.CrudRepository;

import com.inventario.inventario.modelo.Venta;

public interface VentaDAO extends CrudRepository<Venta,Long>{
    
}
