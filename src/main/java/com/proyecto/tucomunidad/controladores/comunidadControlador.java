package com.proyecto.tucomunidad.controladores;

import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Servicios.ComunidadService;
import com.proyecto.tucomunidad.entidades.Comunidad;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class comunidadControlador {

    @Autowired
    ComunidadService comunidadService;

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/comunidad/mostrar")
    public String comunidad(ModelMap modelo, HttpSession session) throws ErrorServicio {
        modelo.put("session", session);
        List<Comunidad> comunidades = comunidadService.listarComunidades();
        modelo.put("comunidades", comunidades);
        modelo.put("crear", null);
        modelo.put("masInfo", "si");
        return "comunidad.html";
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/comunidad-crear")
    public String viviendaCrear(ModelMap modelo, HttpSession session) {
        modelo.put("session", session);
        modelo.put("crear", "si");
        modelo.put("masInfo", null);
        List<Comunidad> comunidades = comunidadService.listarComunidades();
        modelo.put("comunidades", comunidades);
        return "comunidad.html";

    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("/comunidad-creando")
    public String comunidadCrear2(ModelMap modelo, @RequestParam String nombre, @RequestParam String ciudad, @RequestParam String pais, HttpSession session) throws ErrorServicio {
        modelo.put("session", session);
        try {
            comunidadService.registrar(nombre, ciudad, pais);
        } catch (ErrorServicio ex) {
            Logger.getLogger(viviendaControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return comunidad(modelo, session);
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/oficio/{idcomunidad}/eliminarcomunidad")
    public String eliminar(ModelMap modelo, @PathVariable String idcomunidad, HttpSession session) throws ErrorServicio {
        modelo.put("session", session);
        modelo.put("idcomunidad", idcomunidad);
        try {
            comunidadService.darDeBaja(idcomunidad);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("session", session);
            modelo.put("idcomunidad", idcomunidad);
            return comunidad(modelo, session);
        }
        modelo.put("crear", null);
        modelo.put("masInfo", "si");
        return comunidad(modelo, session);

    }
}
