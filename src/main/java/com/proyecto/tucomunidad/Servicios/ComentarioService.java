package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.ComentarioRepo;
import com.proyecto.tucomunidad.entidades.Comentario;
import com.proyecto.tucomunidad.entidades.Proyecto;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {
    @Autowired
    private ComentarioRepo comentarioRepositorio;
    @Autowired
    private ProyectService proyectoService;



    @Transactional
    public Comentario crearComentario(String comentarioEnSi) throws ErrorServicio{
        Comentario comentario = new Comentario();

        
        comentario.setFechaAlta(new Date());
        comentario.setActivo(true);
        comentario.setComentario(comentarioEnSi);

        comentarioRepositorio.save(comentario);
        
        return comentario;
    }

    @Transactional
    public void modificarComentario(String idComentario, String comentarioEnSi) throws ErrorServicio{
        Optional <Comentario> respuesta = comentarioRepositorio.findById(idComentario);

        if (respuesta.isPresent()) {
            Comentario comentario = respuesta.get();
            if (comentarioEnSi.equals(null)) {
            throw new ErrorServicio("El comentario no puede estar vacio");
            }
            comentario.setModificacion(new Date());
            comentario.setComentario(comentarioEnSi);
            comentarioRepositorio.save(comentario);
        }else {
            throw new ErrorServicio("No se encontro el comentario");
        }

    }

    @Transactional
    public void eliminarComentario(String id) throws ErrorServicio{
        Optional <Comentario> respuesta = comentarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Comentario comentario = respuesta.get();
            comentario.setActivo(false);
            comentarioRepositorio.save(comentario);
        }else{
            throw new ErrorServicio("No se encontro el comentario solicitado");
        }

    }

    public Comentario buscarPorId(String id)throws ErrorServicio{
        Optional <Comentario> respuesta = comentarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            return respuesta.get();
        }else{
            throw new ErrorServicio("No se encontro el comentario solicitado");
        }
    }


}