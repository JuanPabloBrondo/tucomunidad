package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Enumeraciones.Eleccion;
import com.proyecto.tucomunidad.Enumeraciones.Estado;
import com.proyecto.tucomunidad.Enumeraciones.Resultado;
import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.ComunidadRepo;
import com.proyecto.tucomunidad.Repositorios.ProyectoRepo;
import com.proyecto.tucomunidad.Repositorios.ViviendaRepo;
import com.proyecto.tucomunidad.Repositorios.VotacionRepo;
import com.proyecto.tucomunidad.entidades.Comunidad;
import com.proyecto.tucomunidad.entidades.Proyecto;
import com.proyecto.tucomunidad.entidades.Vivienda;
import com.proyecto.tucomunidad.entidades.Votacion;
import com.proyecto.tucomunidad.entidades.Voto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotacionService { //registrar, modficar, baja, buscarPorId, alta

    @Autowired
    private VotacionRepo votacionRepo;
    @Autowired
    private ProyectService proyectoService;
    @Autowired
    private ViviendaRepo viviendaRepo;
    @Autowired
    private ComunidadRepo comunidadRepo;
    @Autowired
    private ProyectoRepo proyectoRepo;

    @Transactional
    public Votacion crear(Date fechaInicio, Date fechaFin, Integer quorum, String idComunidad) throws ErrorServicio {

        validar(fechaInicio, fechaFin, quorum);
        Votacion votacion = new Votacion();
        votacion.setFechaInicio(fechaInicio);
        votacion.setFechaFin(fechaFin);
        votacion.setActivo(true);
        votacion.setQuorum(quorum);
        votacionRepo.save(votacion);

        return votacion;
    }

    @Transactional
    public void agregarViviendaVotante(String idVotacion, String idViviendaVota) throws ErrorServicio {

        Optional<Votacion> respuestaVotacion = votacionRepo.findById(idVotacion);
        Optional<Vivienda> respuestaVivienda = viviendaRepo.findById(idViviendaVota);
        if (respuestaVotacion.isPresent() && respuestaVivienda.isPresent()) {
            Votacion votacion = respuestaVotacion.get();
            Vivienda viviendaVotante = respuestaVivienda.get();
            List<Vivienda> votantesEfectivos = votacion.getVotantes();
            votantesEfectivos.add(viviendaVotante);
            votacion.setVotantes(votantesEfectivos);

            votacion.setFechaModificacion(new Date());
            votacionRepo.save(votacion);
        } else {
            if (!respuestaVotacion.isPresent()) {
                throw new ErrorServicio("No se encontró la votación solicitada");
            } else {
                throw new ErrorServicio("No se encontró la vivienda solicitada");
            }
        }
    }

    @Transactional
    public void modificar(String idVotacion, Date fechaInicio, Date fechaFin, List<Voto> votos, Resultado resultado, Integer quorum) throws ErrorServicio {

        validar(fechaInicio, fechaFin, resultado, quorum);
        Optional<Votacion> respuesta = votacionRepo.findById(idVotacion);
        if (respuesta.isPresent()) {
            Votacion votacion = respuesta.get();
            votacion.setFechaInicio(fechaInicio);
            votacion.setFechaFin(fechaFin);
            votacion.setVotos(votos);
            votacion.setResultado(resultado);
            votacion.setQuorum(quorum);
            votacion.setFechaModificacion(new Date());
            votacionRepo.save(votacion);

        } else {
            throw new ErrorServicio("No se encontro la votacion solicitada");
        }

    }

    @Transactional
    public void darBaja(String idVotacion) throws ErrorServicio {

        Optional<Votacion> respuesta = votacionRepo.findById(idVotacion);
        if (respuesta.isPresent()) {
            Votacion votacion = respuesta.get();
            votacion.setActivo(false);
        } else {
            throw new ErrorServicio("No se encontro la votacion solicitada");
        }

    }

    @Transactional
    public void darAlta(String idVotacion) throws ErrorServicio {
        Optional<Votacion> respuesta = votacionRepo.findById(idVotacion);
        if (respuesta.isPresent()) {
            Votacion votacion = respuesta.get();
            votacion.setActivo(true);
        } else {
            throw new ErrorServicio("No se encontro la votacion solicitada");
        }
    }

    public Votacion buscarPorId(String idVotacion) throws ErrorServicio {
        Optional<Votacion> respuesta = votacionRepo.findById(idVotacion);
        if (respuesta.isPresent()) {
            Votacion votacion = respuesta.get();
            return votacion;
        } else {
            throw new ErrorServicio("No se encontro la votacion solicitada");
        }

    }

    private void validar(Date fechaInicio, Date fechaFin, Resultado resultado, Integer quorum) throws ErrorServicio {
        if (fechaFin == null) {
            throw new ErrorServicio("La fecha de fin no puede ser nula");
        }
        if (fechaInicio == null) {
            throw new ErrorServicio("La fecha de inicio no puede ser nula");
        }

        if (resultado == null) {
            throw new ErrorServicio("El resultado no puede ser nulo");
        }
        if (quorum == null) {
            throw new ErrorServicio("El Quorum no puede ser nulo");
        }

    }

    private void validar(Date fechaInicio, Date fechaFin, Integer quorum) throws ErrorServicio {
        if (fechaFin == null) {
            throw new ErrorServicio("La fecha de fin no puede ser nula");
        }
        if (fechaInicio == null) {
            throw new ErrorServicio("La fecha de inicio no puede ser nula");
        }
        if (quorum == null) {
            throw new ErrorServicio("El Quorum no puede ser nulo");
        }

    }

    @Transactional
    public void cambiarResultado(String idProyecto) throws ErrorServicio {
        Proyecto proyecto = proyectoService.buscarPorId(idProyecto);

        Optional<Votacion> respuesta = votacionRepo.findById(proyecto.getVotacion().getId());

        if (respuesta.isPresent()) {
            Votacion votacion = respuesta.get();
            if (new Date().after(votacion.getFechaFin())) {
                if (votacion.getVotos().size() >= votacion.getQuorum()) {
                    boolean v = resultadoVotacion(votacion.getVotos());
                    if (v) {
                        votacion.setResultado(Resultado.A_FAVOR);
                        proyectoService.proyectoAprobado(idProyecto);
                    } else {
                        proyectoService.proyectoDesaprobado(idProyecto);
                        votacion.setResultado(Resultado.EN_CONTRA);
                    }

                } else {
                    proyectoService.proyectoDesaprobado(idProyecto);
                    votacion.setResultado(Resultado.EN_CONTRA);
                    //darBaja(proyecto.getVotacion().getId());
                    votacionRepo.save(votacion);
                }
            }
        } else {
            throw new ErrorServicio("No se encontro la votacion.");
        }

    }

    public boolean resultadoVotacion(List<Voto> votos) {
        int cont1 = 0;
        int cont2 = 0;

        for (Voto v : votos) {
            if (v.getEleccion().equals(Eleccion.POSITIVO)) {
                cont1++;
            } else {
                cont2++;
            }
        }
        if (cont1 > cont2) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void nuevoVoto(Voto voto, String idVotacion) throws ErrorServicio {
        Optional<Votacion> respuesta = votacionRepo.findById(idVotacion);

        if (respuesta.isPresent()) {
            Votacion votacion = respuesta.get();

            List<Voto> votos = votacion.getVotos();
            votos.add(voto);
            votacion.setVotos(votos);

            votacionRepo.save(votacion);
        } else {
            throw new ErrorServicio("No se encontro la votacion.");
        }
    }

    public List<Proyecto> verificarViviendaVoto(String idVivienda) throws ErrorServicio {

        Comunidad comunidad = comunidadRepo.buscarComunidadPorVivienda(idVivienda);
        Vivienda vivienda = viviendaRepo.getOne(idVivienda);

        List< Proyecto> proyectos = proyectoRepo.buscarProyectosPorComunidad(comunidad.getId());

        List<Proyecto> proyectosFaltantes = new ArrayList();

        try {
            for (Proyecto p : proyectos) {

                if (p.getVotacion().getVotantes() != null) {
                    if (!p.getVotacion().getVotantes().contains(vivienda)) {
                        proyectosFaltantes.add(p);
                    }
                } else {
                    proyectosFaltantes.add(p);
                }
            }
        } catch (Exception e) {
            System.out.println("error en el servicio votacion");
        }
        return proyectosFaltantes;

    }
    
    public List <Proyecto> mostrarPropuesta(String idVivienda)throws ErrorServicio{
        List<Proyecto> lista = verificarViviendaVoto(idVivienda);
        List<Proyecto> proyectos = new ArrayList<>();
        for (Proyecto p : lista) {
            if (p.getEstado() == Estado.PROPUESTA) {
                proyectos.add(p);
            }
        }
        return proyectos;
    }

}
