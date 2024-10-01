
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
@Table(name  = "ganancia")
public class Ganancia implements Serializable{
    
    private static final Long UIDSerializable = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idganancia;
    
    private Long idventa; 
    private Date fechadeventa;
    private int total;
    
}
