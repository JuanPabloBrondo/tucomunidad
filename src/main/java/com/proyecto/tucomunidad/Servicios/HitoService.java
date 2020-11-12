/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.ComunidadRepo;
import com.proyecto.tucomunidad.Repositorios.HitoRepo;
import com.proyecto.tucomunidad.entidades.Comunidad;
import com.proyecto.tucomunidad.entidades.Foto;
import com.proyecto.tucomunidad.entidades.Hito;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author carit
 */

@Service
public class HitoService{ //crear, modificar, eliminar
    
    @Autowired
    private HitoRepo hitoRepo;
    @Autowired
    private FotoService fotoService;
    @Autowired
    private ComunidadService comunidadService;
    
    @Autowired
    private ComunidadRepo comunidadRepo;
    
    @Autowired
    private ProyectService proyectoService;
    
    @Transactional 
    public void crear(String fech, String nombre, String descripcion, String idComunidad, MultipartFile archivo) throws ErrorServicio{     

        Date fecha = proyectoService.aFecha(fech);
        
        validar(fecha, nombre, descripcion, idComunidad);
        
        Hito hito = new Hito();
        hito.setFecha(fecha);
        hito.setNombre(nombre);
        hito.setDescripcion(descripcion);
        Foto foto = fotoService.guardar(archivo);
        hito.setFoto(foto);     
        hito.setComunidad(comunidadService.buscarPorId(idComunidad));
        hito.setFechaDeAlta(new Date());
        hito.setActivo(true);
        System.out.println(hito.getNombre());
        hitoRepo.save(hito);
    }
    
    @Transactional
    public void modificar(String id, String fech, String nombre, String descripcion, String idComunidad, MultipartFile archivo) throws ErrorServicio{
        
        Date fecha = proyectoService.aFecha(fech);
        
        validar(fecha, nombre, descripcion, idComunidad);
        
        Optional<Hito> respuesta = hitoRepo.findById(id); 
        if(respuesta.isPresent()){
            Hito hito = respuesta.get();
            hito.setFecha(fecha);
            hito.setNombre(nombre);
            hito.setDescripcion(descripcion);
            Foto foto = fotoService.guardar(archivo);
            hito.setComunidad(comunidadService.buscarPorId(idComunidad));
            hito.setFoto(foto); 
            hito.setFechaDeModificacion(new Date());
        
            hitoRepo.save(hito); 
        }else{ 
            throw new ErrorServicio("No se encontró el hito solicitado");
        }
    }
    
    public void validar(Date fecha, String nombre, String descripcion, String idComunidad) throws ErrorServicio{
        if(fecha == null ){
            throw new ErrorServicio("La fecha del hito no puede ser nula");
        }
        if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre del hito no puede ser nulo");
        }
        if(descripcion == null || descripcion.isEmpty()){
            throw new ErrorServicio("La descripción del hito no puede ser nula");
        }
        if(idComunidad == null || idComunidad.isEmpty()){
            throw new ErrorServicio("La comunidad del hito no puede ser nula");
        }
    }
    
    public Hito buscarPorId(String id)throws ErrorServicio{
        Optional<Hito> respuesta = hitoRepo.findById(id); 
        if(respuesta.isPresent()){
            Hito hito = respuesta.get(); 
            return hito;
        }else{ 
            throw new ErrorServicio("No se encontró el hito solicitado");
            //return null;
        }
    }
    
    @Transactional
    public void darBaja(String id) throws ErrorServicio{        
        Optional<Hito> respuesta = hitoRepo.findById(id); 
        if(respuesta.isPresent()){
            Hito hito = respuesta.get();
            hito.setActivo(false);
            hito.setFechaDeModificacion(new Date());
            hitoRepo.save(hito); 
        }else{ 
            throw new ErrorServicio("No se encontró el hito solicitado");
        }
    }  
    
    @Transactional
    public void darAlta(String id) throws ErrorServicio{        
        Optional<Hito> respuesta = hitoRepo.findById(id); 
        if(respuesta.isPresent()){
            Hito hito = respuesta.get();
            
            hito.setActivo(true);
            hitoRepo.save(hito); 
        }else{ 
            throw new ErrorServicio("No se encontró el hito solicitado");
        }
    } 
    
    public List<Hito> buscarHitosPorComunidad(String idComunidad)throws ErrorServicio{
        Optional<Comunidad>respuesta = comunidadRepo.findById(idComunidad);
        if (respuesta.isPresent()) {
            List<Hito> hitos = hitoRepo.listarHitosComunidad(idComunidad);
            
            return hitos;
        }else{
            throw new ErrorServicio("No se encontró el hito asociado a esa comunidad");
        }
    }
    
}
