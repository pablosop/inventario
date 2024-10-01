package com.inventario.inventario.modelo;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "productos")
public class Producto implements Serializable{
    
    private static final Long serialVersionUID = 1L; 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idproducto;
    
    
    private String nombreproducto;
    
    
    private int unidad;
    
    
    private float preciodecompra;
    
    
    private float preciodeventa;
    
    
    
}
