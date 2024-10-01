package com.inventario.inventario.modelo;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name="ventas")
public class Venta implements Serializable{
    
    private static final Long UIDSerializableLong = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idventa;   
    
    private float totaldeventa;
    private Date fechadeventa;  
}
