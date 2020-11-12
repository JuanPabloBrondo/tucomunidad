package com.proyecto.tucomunidad.controladores;

import com.proyecto.tucomunidad.Enumeraciones.Eleccion;
import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Servicios.ProyectService;
import com.proyecto.tucomunidad.Servicios.UsuarioService;
import com.proyecto.tucomunidad.Servicios.ViviendaService;
import com.proyecto.tucomunidad.Servicios.VotacionService;
import com.proyecto.tucomunidad.Servicios.VotoService;
import com.proyecto.tucomunidad.entidades.Proyecto;
import com.proyecto.tucomunidad.entidades.Usuario;
import com.proyecto.tucomunidad.entidades.Vivienda;
import com.proyecto.tucomunidad.entidades.Voto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/votacion")
public class votacionControlador {

    @Autowired
    private VotoService votoService;
    @Autowired
    private ProyectService proyectoService;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private VotacionService votacionService;
    
    @GetMapping("/votarProyecto")
    public String votacion(ModelMap modelo, Eleccion eleccion) throws ErrorServicio {
       
        Usuario usuario = usuarioService.getUsuario();

        List<Proyecto> proyectos = new ArrayList();

        try {
        
            proyectos = votacionService.mostrarPropuesta(usuario.getVivienda().getId());

            
        } catch (Exception e) {
            System.out.println(e);
        }

        modelo.put("listaNoVotados", proyectos);

        return "votacion.html";
    }
    
    @GetMapping("/votar/{idProyecto}")
    public String votar(ModelMap modelo, @PathVariable String idProyecto) throws ErrorServicio{
        System.out.println("IDPROYECTO          " + idProyecto);
        Proyecto proyecto = proyectoService.buscarPorId(idProyecto);
        modelo.put("nombre", proyecto.getNombre());
        modelo.put("fechaFin", proyecto.getVotacion().getFechaFin());
        modelo.put("id", proyecto.getId());
        
        modelo.put("positivo", Eleccion.POSITIVO);
        modelo.put("negativo", Eleccion.NEGATIVO);
        
        return "votar.html";
    }

    @PostMapping("/proyectoVotado")
    public String votarProyecto(ModelMap modelo, Eleccion eleccion, @RequestParam String idProyecto) throws ErrorServicio {
        Usuario usuario = usuarioService.getUsuario();
        Proyecto proyecto = proyectoService.buscarPorId(idProyecto);
        System.out.println("id vivienda" + usuario.getVivienda().getId());
        try {
            Voto voto = votoService.registrarVoto(eleccion, usuario.getVivienda().getId());
            votacionService.nuevoVoto(voto, proyecto.getVotacion().getId());
            votacionService.agregarViviendaVotante(proyecto.getVotacion().getId(), usuario.getVivienda().getId());

        } catch (ErrorServicio ex) {
            throw new ErrorServicio(ex.getMessage());
        }

        return votacion(modelo, eleccion);
    }   
}
