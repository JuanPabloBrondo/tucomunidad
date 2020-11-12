package com.proyecto.tucomunidad.controladores;

import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Servicios.ComentarioService;
import com.proyecto.tucomunidad.Servicios.ComunidadService;
import com.proyecto.tucomunidad.Servicios.FotoService;
import com.proyecto.tucomunidad.Servicios.HitoService;
import com.proyecto.tucomunidad.Servicios.UsuarioService;
import com.proyecto.tucomunidad.entidades.Comunidad;
import com.proyecto.tucomunidad.entidades.Foto;
import com.proyecto.tucomunidad.entidades.Hito;
import com.proyecto.tucomunidad.entidades.Usuario;
import java.util.Date;
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
@RequestMapping("/hitos")
public class HitoControlador {

    @Autowired
    HitoService hitoService;
    @Autowired
    FotoService fotoService;
    @Autowired
    ComunidadService comunidadService;
    @Autowired
    ComentarioService comentarioService;
    @Autowired
    UsuarioService usuarioService;

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REGULAR') ")
    @GetMapping("/mostrar")
    public String mostrar(ModelMap modelo, HttpSession session) {
        modelo.put("session", session);

        Usuario usuario = usuarioService.getUsuario();

        try {

            Comunidad comunidad = comunidadService.buscarPorId(usuario.getIdcomunidad());
            modelo.put("comunidad", comunidad);

            List<Hito> lista = hitoService.buscarHitosPorComunidad(usuario.getIdcomunidad());

            modelo.put("verhitos", lista);
            int i = 1;
            for (Hito hito : lista) {
                switch (i) {
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
                i++;
            }
            modelo.put("lista", lista);

            modelo.put("crear", null);
            modelo.put("editar", null);
            modelo.put("ver1", null);
            modelo.put("editar1", null);

        } catch (ErrorServicio ex) {
            System.out.println(ex.getMessage());
        }
        return "hitos.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_REGULAR') ")
    @GetMapping("/masinfo/{idhito}")
    public String masinfo(ModelMap modelo, @PathVariable String idcomunidad,
            @PathVariable String idhito, HttpSession session) {
        modelo.put("session", session);
        Hito hito;
        try {
            hito = hitoService.buscarPorId(idhito);
            modelo.put("hito", hito);
        } catch (ErrorServicio ex) {
            Logger.getLogger(proyectoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        Foto foto;
        try {
            foto = hitoService.buscarPorId(idhito).getFoto();
            modelo.put("foto", foto);
        } catch (ErrorServicio ex) {
            Logger.getLogger(proyectoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        modelo.put("vertodos", null);
        modelo.put("crear", null);
        modelo.put("editar", null);
        modelo.put("ver1", "si");
        modelo.put("editar1", null);
        return "hitos.html";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/crear/{idcomunidad}")
    public String crear(ModelMap modelo, @PathVariable String idcomunidad) {

        modelo.put("idcomunidad", idcomunidad);

        modelo.put("vertodos", null);
        modelo.put("crear", "si");
        modelo.put("editar", null);
        modelo.put("ver1", null);
        modelo.put("editar1", null);
        return "hitos.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/creando")
    public String creando(ModelMap modelo, MultipartFile foto, @RequestParam String nombre, @RequestParam String descripcion,
            @RequestParam String fecha, @RequestParam String idcomunidad, HttpSession session) {

        try {
            hitoService.crear(fecha, nombre, descripcion, idcomunidad, foto);
            modelo.put("session", session);
            modelo.put("vertodos", null);
            modelo.put("crear", "si");
            modelo.put("editar", null);
            modelo.put("ver1", null);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("fecha", fecha);
            modelo.put("nombre", nombre);
            modelo.put("descripcion", descripcion);
            modelo.put("idcomunidad", idcomunidad);
            modelo.put("foto", foto);
            System.out.println(ex.toString());

            return "hitos.html";
        }

        return mostrar(modelo, session);
    }

}
