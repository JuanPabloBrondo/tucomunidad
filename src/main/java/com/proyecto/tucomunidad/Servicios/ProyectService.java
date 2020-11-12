package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Enumeraciones.Eleccion;
import com.proyecto.tucomunidad.Enumeraciones.Estado;
import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.ComunidadRepo;
import com.proyecto.tucomunidad.Repositorios.ProyectoRepo;
import com.proyecto.tucomunidad.Repositorios.UsuarioRepo;
import com.proyecto.tucomunidad.entidades.Comentario;
import com.proyecto.tucomunidad.entidades.Comunidad;
import com.proyecto.tucomunidad.entidades.Foto;
import com.proyecto.tucomunidad.entidades.Proyecto;
import com.proyecto.tucomunidad.entidades.Usuario;
import com.proyecto.tucomunidad.entidades.Vivienda;
import com.proyecto.tucomunidad.entidades.Votacion;
import com.proyecto.tucomunidad.entidades.Voto;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProyectService {
    
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private ComunidadService comunidadService;
    @Autowired
    private ProyectoRepo proyectoRepositorio;
    @Autowired
    private VotacionService votacionService;
    @Autowired
    private FotoService fotoService;
    private Estado estado;
    @Autowired
    private ComentarioService comentarioService;
    @Autowired
    private ComunidadRepo comunidadRepo;
    private Eleccion eleccion;
    @Autowired
    private ViviendaService viviendaService;
    
    @Transactional
    public void crearProyecto(MultipartFile archivo, String nombre, String descripcion, String fechaInicio, String fechaFin, String idUsuario,
            String idComunidad, double presupuesto,
            String fechaInicioVotacion, String fechaFinVotacion, Integer quorum) throws ErrorServicio {
        
        Date fechaInicio1 = aFecha(fechaInicio);
        Date fechaFin1 = aFecha(fechaFin);
        Date fechaInicioVotacion1 = aFecha(fechaInicioVotacion);
        Date fechaFinVotacion1 = aFecha(fechaFinVotacion);
        Proyecto proyecto = new Proyecto();
        
        validar(archivo, nombre, descripcion, fechaInicio1, fechaFin1, idUsuario, idComunidad, presupuesto);
        
        proyecto.setNombre(nombre);
        proyecto.setDescripcion(descripcion);
        proyecto.setFechaInicio(fechaInicio1);
        proyecto.setFechaFin(fechaFin1);
        
        Usuario responsable = usuarioService.buscarPorId(idUsuario);
        proyecto.setResponsable(responsable);
        
        proyecto.setEstado(estado.PROPUESTA);
        
        Comunidad comunidad = comunidadService.buscarPorId(idComunidad);
        proyecto.setComunidad(comunidad);

        //Desde aca se crea la votacion y se le setea al proyecto
        Votacion votacion = votacionService.crear(fechaInicioVotacion1, fechaFinVotacion1, quorum, idComunidad);
        proyecto.setVotacion(votacion);
        
        Foto fotoInicio = fotoService.guardar(archivo);
        proyecto.setFoto(fotoInicio);
        
        proyecto.setPresupuesto(presupuesto);
        
        proyecto.setAlta(new Date());
        
        proyecto.setActivo(true);
        
        proyectoRepositorio.save(proyecto);
    }
    
    public Date aFecha(String fecha) {
        Integer anio, mes, dia;
        anio = Integer.parseInt(fecha.substring(0, 4));
        mes = Integer.parseInt(fecha.substring(5, 7)) - 1;
        dia = Integer.parseInt(fecha.substring(8));
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(anio, mes, dia);
        return cal.getTime();
    }

    //ModificarProyectos
    @Transactional
    public void modificarProyecto(MultipartFile archivo, String nombre, String descripcion, String fechaInicio,
            String fechaFin, String idUsuario, String idComunidad, Double presupuesto, String idProyecto) throws ErrorServicio {
        
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        if (fechaFin == null) {
            throw new ErrorServicio("La fecha de finalizacion no puede ser nula");
        }
        
        if (respuesta.isPresent()) {
            Proyecto proyecto = respuesta.get();
            
            Date fechaFin1 = aFecha(fechaFin);
            Date fechaInicio1 = aFecha(fechaInicio);
            proyecto.setNombre(nombre);
            proyecto.setDescripcion(descripcion);
            proyecto.setFechaInicio(fechaInicio1);
            proyecto.setPresupuesto(presupuesto);
            
            Usuario responsable = usuarioService.buscarPorId(idUsuario);
            proyecto.setResponsable(responsable);
            
            Comunidad comunidad = comunidadService.buscarPorId(idComunidad);
            proyecto.setComunidad(comunidad);

            //llama a la metodo para guardar una lista de fotos.
            //
//            List<Foto> fotos = fotoService.guardarFotos(archivos);
//            agregarFotos(fotos, idProyecto);
            proyecto.setFechaFin(fechaFin1);
            proyecto.setModificacion(new Date());
            proyecto.setEstado(Estado.INICIADO);
            if (!archivo.isEmpty()) {
                Foto fotoInicio = fotoService.guardar(archivo);
                proyecto.setFoto(fotoInicio);
                proyectoRepositorio.save(proyecto);
            }
        } else {
            throw new ErrorServicio("No se encontro el proyecto solicitado");
        }
    }

    //actualiza la lista de fotos
    @Transactional
    public void agregarFotos(MultipartFile archivo, String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        Foto foto = fotoService.guardar(archivo);
        
        if (respuesta.isPresent()) {
            Proyecto proyecto = respuesta.get();
            List<Foto> fotoNueva = proyecto.getFotoProgreso();
            
            fotoNueva.add(foto);
            
            proyecto.setFotoProgreso(fotoNueva);
            
            proyectoRepositorio.save(proyecto);
        } else {
            throw new ErrorServicio("No se pudieron agregar las fotos ");
        }
    }

    //Dar de baja un proyecto
    @Transactional
    public void darBaja(String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        if (respuesta.isPresent()) {
            Proyecto proyecto = respuesta.get();
            proyecto.setActivo(false);
            proyectoRepositorio.save(proyecto);
        } else {
            throw new ErrorServicio("No se encontro el proyecto solicitado");
        }
    }

    //Dar de alta un proyecto
    @Transactional
    public void darAlta(String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        if (respuesta.isPresent()) {
            Proyecto proyecto = respuesta.get();
            proyecto.setActivo(true);
            proyecto.setAlta(new Date());
        } else {
            throw new ErrorServicio("No se encontro el proyecto solicitado");
        }
    }

    //Validar atributos cuando se crea el proyecto
    private void validar(MultipartFile archivo, String nombre, String descripcion, Date fechaInicio, Date fechaFin, String idUsuario,
            String idComunidad, Double presupuesto) throws ErrorServicio {
        
        if (archivo == null) {
            throw new ErrorServicio("La foto no puede estar vacia");
        }
        
        if (nombre == null) {
            throw new ErrorServicio("El nombre no puede estar vacio");
        }
        
        if (descripcion == null) {
            throw new ErrorServicio("La descripcion no puede estar vacia");
        }
        
        if (fechaInicio == null) {
            throw new ErrorServicio("La fecha de inicio no puede ser nula");
        }
        
        if (fechaFin == null) {
            throw new ErrorServicio("La fecha de finalizacion no puede estar nula");
        }
        
        Optional<Usuario> usuario = usuarioRepo.findById(idUsuario);
        if (!usuario.isPresent()) {
            throw new ErrorServicio("El usuario no debe ser nulo");
        }

//        Optional<Votacion>votacion = votacionService.buscarPorId(idVotacion);
//        if (!votacion.isPresent()) {
//            throw new ErrorServicio("La votacion no puede ser nula");
//        }
        Optional<Comunidad> comunidad = comunidadRepo.findById(idComunidad);
        if (!comunidad.isPresent()) {
            throw new ErrorServicio("La comunidad no puede ser nula");
        }
        
        if (presupuesto == 0) {
            throw new ErrorServicio("El presopuesto no puede ser 0");
        }
    }

    //Buscar Proyecto por Id
    public Proyecto buscarPorId(String id) throws ErrorServicio {
        
        Optional<Proyecto> proyecto = proyectoRepositorio.findById(id);
        if (proyecto.isPresent()) {
            return proyecto.get();
        } else {
            throw new ErrorServicio("El proyecto no existe");
            
        }
        
    }

    //Agregar un nuevo comentario
    @Transactional
    public void agregarComentario(String idProyecto, String idComentario) throws ErrorServicio {
        Proyecto proyecto = buscarPorId(idProyecto);
        
        Comentario comentario = comentarioService.buscarPorId(idComentario);
        if (comentario == null) {
            throw new ErrorServicio("El comentario no puede estar vacio");
        }
        
        List<Comentario> listaComentarios = proyecto.getComentarios();
        listaComentarios.add(comentario);
        proyecto.setComentarios(listaComentarios);
        
        proyectoRepositorio.save(proyecto);
    }

    //Eliminar comentario
    @Transactional
    public void eliminarComentario(String idProyecto, String idComentario) throws ErrorServicio {
        Proyecto proyecto = buscarPorId(idProyecto);
        
        Comentario comentario = comentarioService.buscarPorId(idComentario);
        if (comentario == null) {
            throw new ErrorServicio("No se ha encontrado el comentario");
        }
        
        List<Comentario> listaComentarios = proyecto.getComentarios();
        listaComentarios.remove(comentario);
        proyecto.setComentarios(listaComentarios);
        
        proyectoRepositorio.save(proyecto);
        
        comentarioService.eliminarComentario(comentario.getId());
    }

    //busca un proyecto por id de comunidad y devuelve una lista ordenada por fecha de inicio del proyecto 
    public List<Proyecto> buscarProyectosPorComunidad(String idComunidad) throws ErrorServicio {
        Optional<Comunidad> respuesta = comunidadRepo.findById(idComunidad);
        if (respuesta.isPresent()) {
            List<Proyecto> proyectos = proyectoRepositorio.buscarProyectosPorComunidad(idComunidad);
            
            return proyectos;
        } else {
            throw new ErrorServicio("No se encontro el proyecto asociado a esa comunidad");
        }
    }

    //busca los comentarios por proyecto
    public List<Comentario> obtenerComentarioDeProyecto(String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        if (respuesta.isPresent()) {
            List<Comentario> comentarios = proyectoRepositorio.buscarComentarioPorProyecto(idProyecto);
            return comentarios;
        } else {
            throw new ErrorServicio("No se encontro el comentario solicitado");
        }
    }
    
    private Date sumarDiasAFecha(Date fecha, Integer diasprestado) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, diasprestado);
        return calendar.getTime();
    }

    //Devuelve el porcentaje del avance del proyecto en dias
    public Integer progresoProyecto(String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        if (respuesta.isPresent()) {
            Proyecto p = respuesta.get();
            
            Calendar fechaInicio = new GregorianCalendar();
            Calendar fechaFin = new GregorianCalendar();
            Calendar fechaHoy = new GregorianCalendar();

            //Seteo las fechas del proyecto
            fechaInicio.setTime(p.getFechaInicio());
            fechaFin.setTime(p.getFechaFin());
            fechaHoy.setTime(new Date());

            //saco la diferencia en milisegundos
            long milisec = fechaFin.getTimeInMillis() - fechaInicio.getTimeInMillis();
            long milisec2 = fechaHoy.getTimeInMillis() - fechaInicio.getTimeInMillis();

            //transformo los milisegundos en dias
            long diferenciaInicioFin = milisec / 1000 / 60 / 60 / 24;
            long diferenciaInicioHoy = milisec2 / 1000 / 60 / 60 / 24;

            //divido los dias y al resultado lo multiplico por 100
            float division = (float) 100 * diferenciaInicioHoy / diferenciaInicioFin;
            Integer resultado = (int) (division);
            return resultado;
        } else {
            throw new ErrorServicio("no se puede mostrar el progreso del proyecto ya que no existe");
        }
        
    }

    //Trae la cantidad de votos a favor
    public Integer cantidadVotosFavor(String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        if (respuesta.isPresent()) {
            Integer cont=0;
            Proyecto proyecto= respuesta.get();
            for(Voto voto: proyecto.getVotacion().getVotos()){
                if(voto.getEleccion().equals(Eleccion.POSITIVO)){
                    cont++;
                }
            }
            return cont;
        } else {
            throw new ErrorServicio("No se encontro el proyecto");
        }
        
    }
    
    public Integer votosFavorDelTotal(String idProyecto) throws ErrorServicio{
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        if (respuesta.isPresent()) {
            Integer resultado=0;
            Proyecto proyecto= respuesta.get();
            Integer favor=cantidadVotosFavor(proyecto.getId());
            Integer totalVotos=proyecto.getVotacion().getVotos().size();
            
            if(totalVotos>0){
                float numero;
                numero = (float) 100 * favor / totalVotos;
                resultado = (int) (numero);
            }
            
            return resultado;
        } else {
            throw new ErrorServicio("No se encontro el proyecto");
        }
    }

    //Devuelve una lista de proyectos asociados a un administrador
    public List<Proyecto> ProyectoAdmin(String mail) {
        List<Proyecto> proyectos = proyectoRepositorio.buscarProyectoPorAdmin(mail);
        
        return proyectos;
    }
    
    public List<Foto> fotoProgreso(String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        if (respuesta.isPresent()) {
            
            Proyecto proyecto = respuesta.get();
            if (proyecto.getFotoProgreso() == null) {
                throw new ErrorServicio("El proyecto no tiene fotos");
            } else {
                List<Foto> fotos = proyecto.getFotoProgreso();
                return fotos;
            }
            
        } else {
            throw new ErrorServicio("No se encontro el proyecto solicitado");
        }
        
    }
    
    public List<Proyecto> buscarProyectoNoVotadoPorVivienda(String idVivienda) throws ErrorServicio {
        Vivienda respuesta = viviendaService.buscarPorId(idVivienda);
        
        if (respuesta != null) {
            List<Proyecto> proyectos = proyectoRepositorio.buscarProyectoPorVivienda(idVivienda);
            return proyectos;
            
        } else {
            throw new ErrorServicio("No se encontro la vivienda");
        }
        
    }
    
    @Transactional
    public void proyectoAprobado(String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        if (respuesta.isPresent()) {
            Proyecto p = respuesta.get();
            p.setEstado(Estado.APROBADO);
            proyectoRepositorio.save(p);
        } else {
            throw new ErrorServicio("No se encontro el proyecto");
        }
    }
    
    @Transactional
    public void proyectoDesaprobado(String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        if (respuesta.isPresent()) {
            Proyecto p = respuesta.get();
            p.setEstado(Estado.RECHAZADO);
            //darBaja(idProyecto);
            proyectoRepositorio.save(p);
        } else {
            throw new ErrorServicio("No se encontro el proyecto");
        }
    }
    
    @Transactional
    public void estadoIniciado(String idProyecto) {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        if (respuesta.isPresent()) {
            Proyecto p = respuesta.get();
            
            if (new Date().after(p.getFechaInicio())) {
                if (p.getEstado().equals(Estado.APROBADO)) {
                    p.setEstado(Estado.INICIADO);
                    proyectoRepositorio.save(p);
                }
                
            }
            
        }
    }
    
    @Transactional
    public void estadoFinalizado(String idProyecto) throws ErrorServicio {
        Optional<Proyecto> respuesta = proyectoRepositorio.findById(idProyecto);
        
        if (respuesta.isPresent()) {
            Proyecto p = respuesta.get();
            
            if (new Date().after(p.getFechaFin())) {
                p.setEstado(Estado.REALIZADO);
                darBaja(idProyecto);
                
                proyectoRepositorio.save(p);
                
            }
            
        }
    }

    /**
     *
     * @return
     */
    public List<Proyecto> todosLosProyectos() {
        List<Proyecto> proyectos = proyectoRepositorio.findAll();
        return proyectos;
    }
}
