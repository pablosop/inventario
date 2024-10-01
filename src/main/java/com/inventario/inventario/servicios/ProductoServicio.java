package com.inventario.inventario.servicios;


import org.springframework.stereotype.Service;

import com.inventario.inventario.dao.ProductoDAO;
import com.inventario.inventario.modelo.Producto;

@Service
public class ProductoServicio {    
    
    private ProductoDAO productoDao; 

    public ProductoServicio (ProductoDAO productoDao){
        this.productoDao = productoDao;
        }
    
    public void setearUnidades(Producto producto, Long idProducto, int UnidadesRestantes) {
        
        var productoAModificar = productoDao.findById(idProducto);
        
        producto.setIdproducto(productoAModificar.get().getIdproducto());
        producto.setNombreproducto(productoAModificar.get().getNombreproducto());
        producto.setPreciodecompra(productoAModificar.get().getPreciodecompra());
        producto.setPreciodeventa(productoAModificar.get().getPreciodeventa());
        producto.setUnidad(UnidadesRestantes);        
        
        productoDao.save(producto);
    }
    
}
