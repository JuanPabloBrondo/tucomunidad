
package com.proyecto.tucomunidad.controladores;

import com.proyecto.tucomunidad.Enumeraciones.ViviendaTipo;
import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Servicios.ProyectService;
import com.proyecto.tucomunidad.Servicios.UsuarioService;
import com.proyecto.tucomunidad.Servicios.ViviendaService;
import com.proyecto.tucomunidad.entidades.Proyecto;
import com.proyecto.tucomunidad.entidades.Usuario;
import com.proyecto.tucomunidad.entidades.Vivienda;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
//@PreAuthorize("hasAnyRole('ROLE_ADMIN_REGISTRADO')")
@RequestMapping("/")
public class viviendaControlador {
    
    @Autowired
    ViviendaService viviendaService;
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    ProyectService proyectoService;
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/viviendas/mostrar/{idcomunidad}")
    public String vivienda(ModelMap modelo, @PathVariable String idcomunidad) throws ErrorServicio{

        List<Vivienda> viviendas = viviendaService.listarViviendasPorComunidad(idcomunidad);

        modelo.put("masInfo", "si");

        modelo.put("viviendas", viviendas);
        
        
        return "viviendas.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/viviendas-crear/{idComunidad}")
    public String viviendaCrear(ModelMap modelo, @PathVariable String idComunidad) {
        
        modelo.put("idComunidad", idComunidad);
        
        modelo.put("crear", "si");
        modelo.put("masInfo", null);
        
        return "viviendas.html";

    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/viviendas-creando")
    public String viviendaCrear2( ModelMap modelo, @RequestParam String idComunidad, @RequestParam String direccion, @RequestParam String claveVivienda, 
            @RequestParam ViviendaTipo tipo, @RequestParam boolean mascota, @RequestParam boolean duenoHabita, 
            @RequestParam Integer numeroHabitantes, MultipartFile foto, String dueno, String mascotita) throws ErrorServicio {
        
        if (dueno != null) {
            duenoHabita = true;
        } 
        
        if (mascotita != null) {
            mascota = true;
        }

        
        try { 
            viviendaService.registrar(claveVivienda, direccion, idComunidad, tipo, foto, mascota, duenoHabita, numeroHabitantes);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return viviendaCrear(modelo, idComunidad);
        }
            
           return vivienda(modelo, idComunidad);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("viviendas_masinfo/{id}")
    public String viviendaMasInfo(ModelMap modelo, @PathVariable String id) {
        
        modelo.put("ver1", "si");
        modelo.put("crear", null);
        
        try {
            Vivienda vivienda;
            vivienda = viviendaService.buscarPorId(id);
            
            modelo.put("vivienda", vivienda);
            
        } catch (ErrorServicio ex) {
            Logger.getLogger(viviendaControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        return "viviendas.html";
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/viviendas_eliminar/{id}")
    public String darbaja(ModelMap modelo, HttpSession session, @PathVariable String id) throws ErrorServicio {
        modelo.put("session", session);
        try {
            viviendaService.darDeBaja(id);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            System.out.println(ex.getMessage());
        }

        Usuario usuario;
        usuario = usuarioService.getUsuario();
        String idcomunidad = usuario.getIdcomunidad();
        List<Proyecto> lista = proyectoService.ProyectoAdmin(usuario.getMail());

        modelo.put("lista", lista);

        modelo.put("vertodos", null);
        modelo.put("crear", null);
        modelo.put("editar", "si");
        modelo.put("ver1", null);
        modelo.put("editar1", null);
        
        return vivienda(modelo, idcomunidad);
    }
}
