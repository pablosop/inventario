package com.inventario.inventario.modelo;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "ventasxproducto")
public class Ventaxproducto implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idfactura;   
    private String nombreproducto;
    private int idventa;
    private int idproducto;
    
    @NotNull
    private int cantidadesvendidas;
    private int preciodeventa;
     
}
