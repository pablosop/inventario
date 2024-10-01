package com.inventario.inventario.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.inventario.dao.VentaXProductoDAO;
import com.inventario.inventario.modelo.Venta;

@Service
public class VentaXProductoServicio {
    
    @Autowired
    private VentaXProductoDAO ventaxproductoDao; 
    
    public void obtenerDetalleFactura(Venta venta) {
        //Prueba
    }  
    
}
