
package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.ValoracionRepo;
import com.proyecto.tucomunidad.entidades.Usuario;
import com.proyecto.tucomunidad.entidades.Valoracion;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValoracionService {
    
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ValoracionRepo valoracionRepositorio;
   
    //Crear una nueva valoracion
    @Transactional
    public Valoracion crear(String idUsuario, Integer calificacion, String comentario) throws ErrorServicio{
        validar(idUsuario, calificacion, comentario);
        
        Valoracion valoracion = new Valoracion();
        
        valoracion.setActivo(true);
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        valoracion.setUsuario(usuario);
        valoracion.setAlta(new Date());
        valoracion.setCalificacion(calificacion);
        valoracion.setComentario(comentario);
        
        valoracionRepositorio.save(valoracion);
        
        return valoracion;
    }
    
    //modificar una valoracion
    @Transactional
    public void modificar(String idValoracion,String idUsuario, Integer calificacion, String comentario) throws ErrorServicio{
        
        validar(idUsuario, calificacion, comentario);
        Optional<Valoracion>respuesta = valoracionRepositorio.findById(idValoracion);
        
        if (respuesta.isPresent()) {
            Valoracion valoracion = respuesta.get();
            
            valoracion.setModificacion(new Date());
            valoracion.setCalificacion(calificacion);
            valoracion.setComentario(comentario);
            
            valoracionRepositorio.save(valoracion);
        }else {
            throw new ErrorServicio("No se encontro la valoracion solicitada");
        }
    }
    
    //Validar algunos atributos
    private void validar(String idUsuario, Integer calificacion, String comentario) throws ErrorServicio{
        Usuario respuesta = usuarioService.buscarPorId(idUsuario);
        if (respuesta == null) {
            throw new ErrorServicio("No se encuentra el usuario asociado a esta valoracion");
        }
        
        if (calificacion == null) {
            throw new ErrorServicio("La calificacion no puede ser nula");
        }
        
        if (comentario.isEmpty()) {
            throw new ErrorServicio("El comentario no puede ser nulo");
        }
    }
    //dar de baja la valoracion
    @Transactional
    public void baja(String idValoracion) throws ErrorServicio{
        Optional <Valoracion> respuesta = valoracionRepositorio.findById(idValoracion);
        
        if(respuesta.isPresent()){
            Valoracion valoracion = respuesta.get();
            valoracion.setActivo(false);
        }else{
            throw new ErrorServicio("No se encontro la valoracion solicitada");
        }
    }
    
    //Buscar por id
    public Valoracion buscarPorid(String id) throws ErrorServicio{
        Optional<Valoracion> respuesta = valoracionRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            Valoracion valoracion = respuesta.get();
            return valoracion;
        }else{
            throw new ErrorServicio("No se encontro la valoracion solicitada");
        }
    }
}
