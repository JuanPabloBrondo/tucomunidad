package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Enumeraciones.ViviendaTipo;
import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.ComunidadRepo;
import com.proyecto.tucomunidad.entidades.Comunidad;
import com.proyecto.tucomunidad.entidades.Usuario;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComunidadService {

    @Autowired
    private ComunidadRepo comunidadRepo;
    @Autowired
    private ViviendaService viviendaService;

    @Transactional
    public void registrar(String nombre, String ciudad, String pais) throws ErrorServicio {
        validar(nombre, ciudad, pais);
        Comunidad comunidad = new Comunidad();
        comunidad.setNombre(nombre);
        comunidad.setCiudad(ciudad);
        comunidad.setPais(pais);
        comunidad.setFechaAlta(new Date());
        comunidad.setActivo(true);

        comunidadRepo.save(comunidad);
        Comunidad co = comunidadRepo.buscarPorNombre(nombre);
        viviendaService.registrar("admin123", "Cabina del administrador", co.getId(), ViviendaTipo.Casa, null, Boolean.FALSE, Boolean.TRUE, 1);
    }

    @Transactional
    public void modificar(String id, String nombre, String ciudad, String pais, List<Usuario> administrador) throws ErrorServicio {
        validar(nombre, ciudad, pais);

        if (administrador == null || administrador.isEmpty()) {
            throw new ErrorServicio("El administrador no pueder ser nulo");
        }
        Optional<Comunidad> respuesta = comunidadRepo.findById(id);
        if (respuesta.isPresent()) {
            Comunidad comunidad = respuesta.get();
            comunidad.setNombre(nombre);
            comunidad.setCiudad(ciudad);
            comunidad.setPais(pais);
            comunidad.setAdministrador(administrador);
            comunidad.setFechaModificacion(new Date());

            comunidadRepo.save(comunidad);
        } else {
            throw new ErrorServicio("No se encontro la comunidad solicitada");
        }

    }

    @Transactional
    public void darDeBaja(String id) throws ErrorServicio {

        Optional<Comunidad> respuesta = comunidadRepo.findById(id);
        if (respuesta.isPresent()) {
            Comunidad comunidad = respuesta.get();
            comunidad.setActivo(false);
        } else {
            throw new ErrorServicio("No se encontro la vivienda solicitada");
        }

    }

    @Transactional
    public void darDeAlta(String id) throws ErrorServicio {

        Optional<Comunidad> respuesta = comunidadRepo.findById(id);
        if (respuesta.isPresent()) {
            Comunidad comunidad = respuesta.get();
            comunidad.setActivo(true);
        } else {
            throw new ErrorServicio("No se encontro la vivienda solicitada");
        }

    }

    private void validar(String nombre, String ciudad, String pais) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if (ciudad == null || ciudad.isEmpty()) {
            throw new ErrorServicio("La ciudad no puede ser nula");
        }
        if (pais == null || pais.isEmpty()) {
            throw new ErrorServicio("El pais no puede ser nulo");
        }

    }

    public Comunidad buscarPorId(String id) throws ErrorServicio {

        Optional<Comunidad> respuesta = comunidadRepo.findById(id);
        if (respuesta.isPresent()) {
            Comunidad comunidad = respuesta.get();
            return comunidad;
        } else {
            throw new ErrorServicio("No se encontro la comunidad solicitada");
        }

    }

    public List<Comunidad> listarComunidades() {
        List<Comunidad> comunidades = comunidadRepo.listarComunidades();

        return comunidades;
    }

}
