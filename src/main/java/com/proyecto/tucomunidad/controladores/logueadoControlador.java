package com.proyecto.tucomunidad.controladores;

import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Servicios.ComunidadService;
import com.proyecto.tucomunidad.Servicios.HitoService;
import com.proyecto.tucomunidad.Servicios.ProyectService;
import com.proyecto.tucomunidad.Servicios.UsuarioService;
import com.proyecto.tucomunidad.Servicios.VotacionService;
import com.proyecto.tucomunidad.entidades.Comunidad;
import com.proyecto.tucomunidad.entidades.Hito;
import com.proyecto.tucomunidad.entidades.Proyecto;
import com.proyecto.tucomunidad.entidades.Usuario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ROLE_SUPERADMIN') || hasRole('ROLE_ADMIN') || hasRole('ROLE_REGULAR') ")
public class logueadoControlador {

    @Autowired
    ProyectService proyectoService;

    @Autowired
    ComunidadService comunidadService;

    @Autowired
    HitoService hitoService;

    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    VotacionService votacionService;
    

    @GetMapping("/logueado")
    public String ingreso(ModelMap modelo, HttpSession session) throws ErrorServicio {

        try {
            
            modelo.put("session", session);

            Usuario usuario = usuarioService.getUsuario();

            Comunidad comunidad = comunidadService.buscarPorId(usuario.getIdcomunidad());
            modelo.put("comunidad", comunidad);

            List<Proyecto> listaProyectos = proyectoService.buscarProyectosPorComunidad(usuario.getIdcomunidad());
            for(Proyecto proyecto: listaProyectos){
                votacionService.cambiarResultado(proyecto.getId());
                proyectoService.estadoIniciado(proyecto.getId());
                proyectoService.estadoFinalizado(proyecto.getId());
            }
            
            int i = 1;
            for (Proyecto proyecto : listaProyectos) {
                switch (i) {
                    case 1:
                        modelo.put("proyecto1", proyecto);
                        break;
                    case 2:
                        modelo.put("proyecto2", proyecto);
                        break;
                    case 3:
                        modelo.put("proyecto3", proyecto);
                        break;
                }
                i++;
            }
            modelo.put("listaproyectos", listaProyectos);

            List<Hito> listaHitos = hitoService.buscarHitosPorComunidad(usuario.getIdcomunidad());
            
            int j = 1;
            for (Hito hito : listaHitos) {
                switch (j) {
                    case 1:
                        modelo.put("hito1", hito);
                        break;
                    case 2:
                        modelo.put("hito2", hito);
                        break;
                    case 3:
                        modelo.put("hito3", hito);
                        break;
                }
                j++;
            }
            modelo.put("listahitos", listaHitos);

        } catch (ErrorServicio ex) {
            Logger.getLogger(Controladores.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "logueado.html";
    }

    

}
