package com.proyecto.tucomunidad.controladores;

import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Servicios.ComunidadService;
import com.proyecto.tucomunidad.Servicios.UsuarioService;
import com.proyecto.tucomunidad.entidades.Comunidad;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class usuarioControlador {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ComunidadService comunidadService;
    

    @PreAuthorize("hasRole('ROLE_SUPERADMIN') || hasRole('ROLE_ADMIN') ")
    @GetMapping("/usuario_lista/{mail}")
    public String usuarioLista(ModelMap modelo, @PathVariable String mail, HttpSession session) throws ErrorServicio {
        modelo.put("session", session);

        List<Usuario> usuarios = usuarioService.listarUsuariosComunidad(mail);
        List<Usuario> usuariosTodos=usuarioService.listarUsuarios();
        modelo.put("usuariosTodos",usuariosTodos);
        modelo.put("usuarios", usuarios);
        modelo.put("ver", null);
        modelo.put("listar", "si");

        return "usuarios.html";
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN') || hasRole('ROLE_ADMIN') ")
    @GetMapping("/usuario_lista/usuario_eliminar/{mail}")
    public String usuarioEliminar(ModelMap modelo, @PathVariable String mail, HttpSession session) throws ErrorServicio {
        modelo.put("session", session);

        try {
            usuarioService.darBaja(mail);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return usuarioLista(modelo, mail, session);
        }

            return usuarioLista(modelo, mail, session);
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN') || hasRole('ROLE_ADMIN') ")
    @GetMapping("/usuario_lista/usuario_update/{mail}")
    public String usuarioUpdate(ModelMap modelo, @PathVariable String mail, HttpSession session) throws ErrorServicio {
        modelo.put("session", session);

        try {
            usuarioService.asignarAdmin(mail);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return usuarioLista(modelo, mail, session);
        }

        return usuarioLista(modelo, mail, session);
    }

    @GetMapping("/usuario_lista/usuario_ver/{mail}")
    public String usuarioMasInfo(ModelMap modelo, @PathVariable String mail, HttpSession session) {
        modelo.put("session", session);
        modelo.put("ver", "si");
        modelo.put("listar", null);

        try {
            Usuario usuario;
            usuario = usuarioService.buscarPorId(mail);
Comunidad comunidad=comunidadService.buscarPorId(usuario.getIdcomunidad());
            modelo.put("comunidad",comunidad);
            modelo.put("usuario", usuario);

        } catch (ErrorServicio ex) {
            Logger.getLogger(viviendaControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "usuarios.html";
    }

}
