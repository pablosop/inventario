package com.inventario.inventario.controladores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventario.inventario.dao.GananciaDAO;
import com.inventario.inventario.dao.ProductoDAO;
import com.inventario.inventario.dao.VentaDAO;
import com.inventario.inventario.dao.VentaXProductoDAO;
import com.inventario.inventario.modelo.Ganancia;
import com.inventario.inventario.modelo.Producto;
import com.inventario.inventario.modelo.Venta;
import com.inventario.inventario.modelo.Ventaxproducto;
import com.inventario.inventario.servicios.ProductoServicio;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class Controlador {
   
    @Autowired
    private ProductoDAO productoDao; 
    
    @Autowired
    private VentaDAO ventaDao;
    
    @Autowired
    private ProductoServicio productoServicio;
    
    @Autowired
    private VentaXProductoDAO ventaxproductoDao;
    
    @Autowired
    private GananciaDAO gananciaDao; 
    
    //Inicio
    @GetMapping("/")
    public String holaMundo(HttpSession session) {
        session.removeAttribute("productosparaventa");
        return "index"; 
        
    }

    //Listar producto
    @GetMapping("/listarproductos")
    public String listarProductos(Model model, HttpSession session) {
        session.removeAttribute("productosparaventa");
        var productos = productoDao.findAll();
        model.addAttribute("productos", productos);
        return "listarproductos";
    }
    
    //Ir hacia la pagina para agregar el producto
    @GetMapping("/agregarproducto")
    public String agregarProducto() {  
        
    return "agregarproducto";           
 }
  
    //Guardar en BD el producto
    @PostMapping("/guardarproducto")
    public String guardarProducto(Producto producto, Errors errores, Model model) {
        if(errores.hasErrors()) {
             model.addAttribute("producto", producto);
             model.addAttribute("error", "Complete todos los campos");
             return "agregarproducto";
        }
        productoDao.save(producto);
        return "redirect:/listarproductos";
    }   
    
    //Editar producto
    @GetMapping("/editarproducto/{idproducto}")
    public String editarProducto(Producto producto, Model model)
    {
        producto = productoDao.findById(producto.getIdproducto()).orElse(null);
        model.addAttribute("producto", producto);
        return "modificarproducto";
    }  
    
    //Eliminar producto
    @GetMapping("/eliminarproducto/{idproducto}")
    public String eliminarProducto (Producto producto, Errors errores, Model model) {
        productoDao.deleteById(producto.getIdproducto());
        return "redirect:/listarproductos";
    }
    
    
    //----------------------------------------
    
    //Agregar productos a la venta  
    @GetMapping("/agregarproductoventa")
    public String agregarProductoVenta(Model model, HttpSession session) {
       
        var productosParaVenta = (List<Producto>) session.getAttribute("productosparaventa");
        var productosdrop = productoDao.findAll();
        model.addAttribute("productosdrop", productosdrop);
        return "agregarproductoventa";
    }
    
    //Listar las ventas
    @GetMapping("/listarventa")
    public String listarVentas(Model model, HttpSession session) {
        session.removeAttribute("productosparaventa");
        var ventas = ventaDao.findAll();
        model.addAttribute("ventas", ventas);
        return "listarventa";
    }

    //Agregar venta
    @GetMapping("/agregarventa") 
    public String agregarVenta(Model model, HttpSession session) {
        
        var productosParaVenta = (List<Producto>) session.getAttribute("productosparaventa");
    
    
    if (productosParaVenta != null) {
        session.setAttribute("productosparaventa", productosParaVenta);
        model.addAttribute("productosparaventa", productosParaVenta);
    }
        
        
        return "agregarventa";
    }
    
    //Guardar los productos agregador para la venta numero X
        @PostMapping("/guardarventasxproducto")
    public String guardarVentaXProducto(Producto productoNuevo, @RequestParam(required = false) Long productodropdown,@RequestParam(required = false) int cantidadesvendidas, Model model, HttpSession session, Errors errores) {
        
        if(errores.hasErrors() || ((cantidadesvendidas == 0 || cantidadesvendidas < 0) && productodropdown == null))
        {
            var productosdrop = productoDao.findAll();
            model.addAttribute("productosdrop", productosdrop);
            model.addAttribute("error", "Cargue minimo un producto para realizar la venta");        
            model.addAttribute("error2", "La cantidad debe ser mayor a 0");
            return "agregarproductoventa";
        }
        
        if(errores.hasErrors() || productodropdown == null)
        {
            var productosdrop = productoDao.findAll();
            model.addAttribute("productosdrop", productosdrop);
            model.addAttribute("error", "Cargue minimo un producto para realizar la venta");
            return "agregarproductoventa";
        }
        
        if(errores.hasErrors() || cantidadesvendidas == 0 || cantidadesvendidas < 0)
        {
            var productosdrop = productoDao.findAll();
            model.addAttribute("productosdrop", productosdrop);
            model.addAttribute("error2", "La cantidad debe ser mayor a 0");
            return "agregarproductoventa";
        }              
        
        var productosParaVenta = (List<Producto>) session.getAttribute("productosparaventa");
        
        if(productosParaVenta != null) {
            
        int unidadesTotalesProducto = productoDao.findById(productodropdown).orElse(null).getUnidad();
        
        if(cantidadesvendidas > unidadesTotalesProducto) {
            model.addAttribute("errorUnidades", "No hay suficientes unidades disponibles."); 
            session.setAttribute("productosparaventa", productosParaVenta);
            model.addAttribute("productosparaventa", productosParaVenta);

            return "agregarventa";
            
        } else {
            
        productoNuevo.setIdproducto(productoDao.findById(productodropdown).orElse(null).getIdproducto());
        productoNuevo.setNombreproducto(productoDao.findById(productodropdown).orElse(null).getNombreproducto());
        productoNuevo.setPreciodeventa(productoDao.findById(productodropdown).orElse(null).getPreciodeventa());
        productoNuevo.setUnidad(cantidadesvendidas);
        
        productosParaVenta.add(productoNuevo);
        session.setAttribute("productosparaventa", productosParaVenta);
        
        }
        
        } else {
        
        int unidadesTotalesProducto = productoDao.findById(productodropdown).orElse(null).getUnidad();
        
        if(cantidadesvendidas > unidadesTotalesProducto) {
            model.addAttribute("errorUnidades", "No hay suficientes unidades disponibles, el maximo de unidades es " + unidadesTotalesProducto 
                    + " para el producto de " + productoDao.findById(productodropdown).orElse(null).getNombreproducto());
            return "agregarventa";
            
        } else {
        
        
        List <Producto> productosParaVenta2 = new ArrayList<>();
        
        productoNuevo.setIdproducto(productoDao.findById(productodropdown).orElse(null).getIdproducto());
        productoNuevo.setNombreproducto(productoDao.findById(productodropdown).orElse(null).getNombreproducto());
        productoNuevo.setPreciodeventa(productoDao.findById(productodropdown).orElse(null).getPreciodeventa());
        productoNuevo.setUnidad(cantidadesvendidas);
        
        productosParaVenta2.add(productoNuevo);  
        session.setAttribute("productosparaventa", productosParaVenta2);
        }
        
    }     
        
      
    
        return "redirect:/agregarventa";
                                                           
}
    
    //Guardar la venta con sus detalles de venta
    @PostMapping("/guardarventa")
    public String guardarVenta(Ganancia gananciaNueva, Venta ventaNueva, HttpSession session, Errors errores, Model model) {
        
        
        
         float totalForEach = 0; 
         Date fechaDeHoy = new Date();
         var productosParaVenta = (List<Producto>) session.getAttribute("productosparaventa");
         
         if(errores.hasErrors() || productosParaVenta == null)
        {
            model.addAttribute("error", "La venta debe tener minimo un producto");
            return "agregarventa";
        }
         
         for(Producto productoNuevo : productosParaVenta) {
            int unidadesProducto = productoNuevo.getUnidad(); 
            float precioProducto = productoNuevo.getPreciodeventa();
            totalForEach = totalForEach + (unidadesProducto * precioProducto);
         }
         
        ventaNueva.setTotaldeventa(totalForEach);
        ventaNueva.setFechadeventa(fechaDeHoy);
         
        ventaDao.save(ventaNueva);
        
        for(Producto productoNuevo : productosParaVenta) {
        // Crear una nueva instancia de ventaxproducto para cada iteración
        Ventaxproducto ventaxproductoNuevo = new Ventaxproducto();

        int idProducto = productoNuevo.getIdproducto().intValue();
        int unidadesProducto = productoNuevo.getUnidad(); 
        float precioProducto = productoNuevo.getPreciodeventa();
        int idVenta = ventaNueva.getIdventa().intValue();
        String nombreproducto = productoNuevo.getNombreproducto();
                        
        Long idProductoServicio = productoNuevo.getIdproducto();
        int cantidadProductoTotal = productoDao.findById(idProductoServicio).orElse(null).getUnidad();
                        
        // Asignar los valores al nuevo objeto
        ventaxproductoNuevo.setIdproducto(idProducto);
        ventaxproductoNuevo.setCantidadesvendidas(unidadesProducto);
        ventaxproductoNuevo.setPreciodeventa((int) precioProducto);
        ventaxproductoNuevo.setIdventa(idVenta);
        ventaxproductoNuevo.setNombreproducto(nombreproducto);

        // Guardar el nuevo objeto en la base de datos
        ventaxproductoDao.save(ventaxproductoNuevo);  
        
        int unidadesRestantes = cantidadProductoTotal - unidadesProducto;
        
        productoServicio.setearUnidades(productoNuevo, idProductoServicio, unidadesRestantes);
        
        gananciaNueva.setIdventa(ventaNueva.getIdventa());
        gananciaNueva.setFechadeventa(ventaNueva.getFechadeventa());
        gananciaNueva.setTotal((int) ventaNueva.getTotaldeventa());
        gananciaDao.save(gananciaNueva);
        
        System.out.println(ventaxproductoNuevo);
    }
        
        session.removeAttribute("productosparaventa");
        
        
        
        return "redirect:/listarventa";
                                                           
}
    
    //Listar las ganancias generadas
    @GetMapping("/listarganancia")
    public String obtenerGanancias(Model model, HttpSession session) {
        
        session.removeAttribute("productosparaventa");
        
        Date fechaHoy = new Date();
        List <Ganancia> gananciasFiltradas = new ArrayList<>();
        var ganancias = gananciaDao.findAll();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        for(Ganancia ganancia : ganancias) {
           String fechaHoyString = formato.format(fechaHoy);
           String fechaGanancia = formato.format(ganancia.getFechadeventa());
            if(fechaHoyString.equals(fechaGanancia)) {
                gananciasFiltradas.add(ganancia);
            }
        }
        var gananciasFiltroRealizado = gananciasFiltradas;
        System.out.println(gananciasFiltradas.toString());
        model.addAttribute("fechahoy", fechaHoy);
        model.addAttribute("ganancias", gananciasFiltroRealizado);       
        return "listarganancia";
    }
    
    
    //Visualizar el detalle de la factura
    @GetMapping("/verventaxproducto/{idventa}")
    public String verDetallesFactura(Venta venta, Model model)
    {
        
        venta = ventaDao.findById(venta.getIdventa()).orElse(null);
        
        List <Ventaxproducto> listaFiltrada = new ArrayList<>();
        
        var detalleFacturaCompleto = ventaxproductoDao.findAll();
        
        for(Ventaxproducto detalleFactura : detalleFacturaCompleto) {
            if (detalleFactura.getIdventa() == venta.getIdventa()) {
                listaFiltrada.add(detalleFactura); 
            }
        }        
        var detallesCompletos = listaFiltrada;
        
        model.addAttribute("numerodeventa", ventaDao.findById(venta.getIdventa()).orElse(null).getIdventa());
        model.addAttribute("detallescompletos", detallesCompletos);
        return "verventaxproducto";
    }


   
    
}
