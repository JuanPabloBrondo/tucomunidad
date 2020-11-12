/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Enumeraciones.Categoria;
import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.OficioRepo;
import com.proyecto.tucomunidad.entidades.Oficio;
import com.proyecto.tucomunidad.entidades.Valoracion;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carit
 */
@Service
public class OficioService { //crear, modificar, eliminar, agregarValoración
//    private String id;
//    private Categoria categoria;
//    private String nombre; 
//    private String contacto;
//    private List<Valoracion> valoraciones;
//    private Usuario publicador;
    @Autowired
    private OficioRepo oficioRepo;
    @Autowired
    private UsuarioService usuarioService; 
    
    @Transactional
    public void crear(Categoria categoria, String nombre, String contacto) throws ErrorServicio{     
        validar(categoria, nombre, contacto);
        
        Oficio oficio = new Oficio();
        oficio.setCategoria(categoria);
        oficio.setNombre(nombre);
        oficio.setContacto(contacto);
        oficio.setPublicador(usuarioService.getUsuario());
        oficio.setFechaDeAlta(new Date());
        oficio.setActivo(true);
   
        oficioRepo.save(oficio);
    }
    
    @Transactional
    public void modificar(String id, Categoria categoria, String nombre, String contacto, List<Valoracion> valoraciones) throws ErrorServicio{
        validar(categoria, nombre, contacto);
        
        Optional<Oficio> respuesta = oficioRepo.findById(id); 
        if(respuesta.isPresent()){
            Oficio oficio = respuesta.get();
            oficio.setCategoria(categoria);
            oficio.setNombre(nombre);
            oficio.setContacto(contacto);
            oficio.setValoraciones(valoraciones);
            oficio.setFechaDeModificacion(new Date());
        
            oficioRepo.save(oficio); 
        }else{ 
            throw new ErrorServicio("No se encontró el oficio solicitado");
        }
    }
    
    @Transactional
    public void agregarValoracion(String id, Valoracion valoracion) throws ErrorServicio{
        
        Optional<Oficio> respuesta = oficioRepo.findById(id); 
        if(respuesta.isPresent()){
            Oficio oficio = respuesta.get();
            List <Valoracion> valoraciones = oficio.getValoraciones();
            valoraciones.add(valoracion);
            oficio.setValoraciones(valoraciones);
            oficio.setFechaDeModificacion(new Date());
        
            oficioRepo.save(oficio); 
        }else{ 
            throw new ErrorServicio("No se encontró el oficio solicitado");
        }
    }
    
    public void validar(Categoria categoria, String nombre, String contacto) throws ErrorServicio{
        if(categoria == null){
            throw new ErrorServicio("La categoría del oficio no puede ser nula");
        }
        if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre del oficio no puede ser nulo");
        }
        if(contacto == null || contacto.isEmpty()){
            throw new ErrorServicio("El contacto del oficio no puede ser nulo");
        }
    }
    
    @Transactional
    public void darBaja(String id) throws ErrorServicio{        
        Optional<Oficio> respuesta = oficioRepo.findById(id); 
        if(respuesta.isPresent()){
            Oficio oficio = respuesta.get();
            
            oficio.setActivo(false);
            oficio.setFechaDeModificacion(new Date());
            oficioRepo.save(oficio); 
        }else{ 
            throw new ErrorServicio("No se encontró el oficio solicitado");
        }
    }    
    
    @Transactional
    public void darAlta(String id) throws ErrorServicio{        
       Optional<Oficio> respuesta = oficioRepo.findById(id); 
        if(respuesta.isPresent()){
            Oficio oficio = respuesta.get();
            
            oficio.setFechaDeModificacion(new Date());
            oficio.setActivo(true);
            oficioRepo.save(oficio); 
        }else{ 
            throw new ErrorServicio("No se encontró el oficio solicitado");
        }
    } 
    
    public List<Oficio> buscarOficiosPorComunidad(String idComunidad) throws ErrorServicio{
        List<Oficio> oficios = oficioRepo.listarOficiosComunidad(idComunidad);
        return oficios;
    }
    
    
    public Oficio buscarPorId(String id) throws ErrorServicio {
        Optional<Oficio> respuesta = oficioRepo.findById(id);
        if (respuesta.isPresent()) {
            Oficio oficio = respuesta.get();
            return oficio;
        } else {
            throw new ErrorServicio("No se encontró el oficio solicitado");
            //return null;
        }
    }
    
    public Integer promedioValoracion(String idOficio) throws ErrorServicio {
        Optional<Oficio> respuesta = oficioRepo.findById(idOficio);
        if (respuesta.isPresent()) {
            Oficio oficio = respuesta.get();
            List<Valoracion> valoraciones = oficio.getValoraciones();
            Integer sumaCalificacion=0;
            for (Valoracion valoracion : valoraciones) {
                sumaCalificacion+=valoracion.getCalificacion();
            }            
            if(valoraciones.isEmpty()){
                return null;
            }
            return sumaCalificacion/valoraciones.size();
        } else {
            throw new ErrorServicio("No se encontró el oficio solicitado");
            //return null;
        }
    }

}
