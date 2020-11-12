package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Enumeraciones.ViviendaTipo;
import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.ViviendaRepo;
import com.proyecto.tucomunidad.entidades.Comunidad;
import com.proyecto.tucomunidad.entidades.Foto;
import com.proyecto.tucomunidad.entidades.Usuario;
import com.proyecto.tucomunidad.entidades.Vivienda;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ViviendaService {

    @Autowired
    private ViviendaRepo viviendaRepo;
    @Autowired
    private ComunidadService comunidadservice;
    @Autowired
    private FotoService fotoService;
    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public void registrar(String claveVivienda, String direccion, String idComunidad, ViviendaTipo tipo, MultipartFile fotoNueva, Boolean mascota, Boolean duenoHabita, Integer numeroHabitantes) throws ErrorServicio {

        validar(claveVivienda, direccion, idComunidad, tipo, fotoNueva, mascota, duenoHabita, numeroHabitantes);

        Vivienda vivienda = new Vivienda();
        vivienda.setClaveVivienda(claveVivienda);
        vivienda.setDireccion(direccion);
        Comunidad comunidad = comunidadservice.buscarPorId(idComunidad);
        vivienda.setComunidad(comunidad);
        vivienda.setTipo(tipo);
        Foto foto = fotoService.guardar(fotoNueva);
        vivienda.setFoto(foto);
        vivienda.setMascota(mascota);
        vivienda.setDuenoHabita(duenoHabita);
        vivienda.setNumeroHabitantes(numeroHabitantes);
        vivienda.setFechaAlta(new Date());
        vivienda.setActivo(true);
        viviendaRepo.save(vivienda);

    }

    @Transactional
    public void modificar(String id, String claveVivienda, String direccion, String idComunidad, ViviendaTipo tipo, MultipartFile fotoNueva, Boolean mascota, Boolean duenoHabita, Integer numeroHabitantes) throws ErrorServicio {
       
        validar(claveVivienda, direccion, idComunidad, tipo, fotoNueva, mascota, duenoHabita, numeroHabitantes);
        Optional<Vivienda> respuesta = viviendaRepo.findById(id);
        if (respuesta.isPresent()) {
            Vivienda vivienda = respuesta.get();
            vivienda.setClaveVivienda(claveVivienda);
            vivienda.setDireccion(direccion);
            Comunidad comunidad = comunidadservice.buscarPorId(idComunidad);
            vivienda.setComunidad(comunidad);
            vivienda.setTipo(tipo);
            Foto foto = fotoService.guardar(fotoNueva);
            vivienda.setFoto(foto);
            vivienda.setMascota(mascota);
            vivienda.setDuenoHabita(duenoHabita);
            vivienda.setNumeroHabitantes(numeroHabitantes);
            vivienda.setFechaModificacion(new Date());
            viviendaRepo.save(vivienda);
        } else {
            throw new ErrorServicio("No se encontro la vivienda solicitada");
        }

    }

    @Transactional
    public void darDeBaja(String id) throws ErrorServicio {
      
        Optional<Vivienda> respuesta = viviendaRepo.findById(id);
        if (respuesta.isPresent()) {
            Vivienda vivienda = respuesta.get();
            vivienda.setActivo(false);
        } else {
            throw new ErrorServicio("No se encontro la vivienda solicitada");
        }

    }

    @Transactional
    public void darDeAlta(String id) throws ErrorServicio {
       
        Optional<Vivienda> respuesta = viviendaRepo.findById(id);
        if (respuesta.isPresent()) {
            Vivienda vivienda = respuesta.get();
            vivienda.setActivo(true);
        } else {
            throw new ErrorServicio("No se encontro la vivienda solicitada");
        }

    }

    private void validar(String claveVivienda, String direccion, String idComunidad, ViviendaTipo tipo, MultipartFile foto, Boolean mascota, Boolean duenoHabita, Integer numeroHabitantes) throws ErrorServicio {
        if (claveVivienda == null || claveVivienda.isEmpty() || claveVivienda.length() <= 6) {
            throw new ErrorServicio("La clave de la vivienda no puede ser nula y tiene que tener mas de 6 digitos");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new ErrorServicio("La direccion no puede ser nula");
        }
        if (idComunidad == null || idComunidad.isEmpty()) {
            throw new ErrorServicio("La comunidad no puede ser nula");
        }
        if (tipo == null) {
            throw new ErrorServicio("El tipo de vivienda no puede ser nula");
        }
        if (mascota == null) {
            throw new ErrorServicio("La mascota no puede ser nula");
        }
        if (duenoHabita == null) {
            throw new ErrorServicio("El dueño no puede ser nulo");
        }
        if (numeroHabitantes == null) {
            throw new ErrorServicio("El numero de habitantes no puede ser nulo");
        }

    }
 
    public Vivienda buscarPorId(String id) throws ErrorServicio {
       
        Optional<Vivienda> respuesta = viviendaRepo.findById(id);
        if (respuesta.isPresent()) {
            Vivienda vivienda = respuesta.get();
            return vivienda;
        } else {
            throw new ErrorServicio("No se encontro la vivienda solicitada");
        }

    }

    public List<Vivienda> listarViviendasPorComunidad(String idComunidad) {
        List<Vivienda> viviendas = viviendaRepo.listarViviendasPorComunidad(idComunidad);
        
        return viviendas;
    }


}
