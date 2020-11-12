package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Enumeraciones.Rol;
import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.UsuarioRepo;
import com.proyecto.tucomunidad.entidades.Foto;
import com.proyecto.tucomunidad.entidades.Usuario;
import com.proyecto.tucomunidad.entidades.Vivienda;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioService implements UserDetailsService { //registrar, modficar, baja, buscarPorId, alta

    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private ViviendaService viviendaService;
    @Autowired
    private FotoService fotoService;
    @Autowired
    private ComunidadService comunidadService;

    @Transactional
    public void registrar(String mail, String nombre, String apellido, String clave1, String clave2, String idVivienda, String claveVivienda, String telefono, MultipartFile archivo) throws ErrorServicio {
        validar(mail, nombre, apellido, clave1, clave2, idVivienda, claveVivienda, telefono);

        Usuario usuario = new Usuario();
        usuario.setMail(mail);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        String encriptada = new BCryptPasswordEncoder().encode(clave1);
        usuario.setClave(encriptada);
        Vivienda vivienda = viviendaService.buscarPorId(idVivienda);
        usuario.setIdcomunidad(vivienda.getComunidad().getId());
        usuario.setVivienda(vivienda);
        usuario.setTelefono(telefono);
        Foto foto = fotoService.guardar(archivo);
        usuario.setFoto(foto);
        usuario.setFechaDeAlta(new Date());
        usuario.setActivo(true);
        usuario.setTipo(Rol.REGULAR);

        usuarioRepo.save(usuario);
    }

    @Transactional
    public void modificar(String mail, String nombre, String apellido,String idVivienda, String claveVivienda, String clave1, String clave2, String telefono, MultipartFile archivo) throws ErrorServicio {
        validar(mail, nombre, apellido, clave1, clave2, idVivienda, claveVivienda, telefono);
        System.out.println(clave1+"   "+clave2);
        Optional<Usuario> respuesta = usuarioRepo.findById(mail);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            String encriptada = new BCryptPasswordEncoder().encode(clave1);
            usuario.setClave(encriptada);
            usuario.setTelefono(telefono);
            Foto foto = fotoService.guardar(archivo);
            usuario.setFoto(foto);
            usuario.setFechaDeModificacion(new Date());
            Vivienda vivienda = viviendaService.buscarPorId(idVivienda);
            usuario.setVivienda(vivienda);
            
            usuarioRepo.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }

    @Transactional
    public void asignarAdmin(String mail) throws ErrorServicio {
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail del usuario no puede ser nulo");
        }
        Optional<Usuario> respuesta = usuarioRepo.findById(mail);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setTipo(Rol.ADMIN);
            usuarioRepo.save(usuario);

            List<Usuario> administradores = comunidadService.buscarPorId(usuario.getVivienda().getComunidad().getId()).getAdministrador();
            administradores.add(usuario);

            comunidadService.modificar(usuario.getVivienda().getComunidad().getId(), usuario.getVivienda().getComunidad().getNombre(), usuario.getVivienda().getComunidad().getCiudad(), usuario.getVivienda().getComunidad().getPais(), administradores);

        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }

    public void validar(String mail, String nombre, String apellido, String clave1, String clave2, String telefono) throws ErrorServicio {
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail del usuario no puede ser nulo");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo");
        }
        if (clave1 == null || clave1.isEmpty() || clave2 == null || clave2.isEmpty()) {
            throw new ErrorServicio("La clave del usuario no puede ser nula");
        }
        if (!clave1.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new ErrorServicio("El teléfono no puede ser nulo");
        }
    }

    public void validar(String mail, String nombre, String apellido, String clave1, String clave2, String idVivienda, String claveVivienda, String telefono) throws ErrorServicio {
        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail del usuario no puede ser nulo");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido no puede ser nulo");
        }
        if (clave1 == null || clave1.isEmpty() || clave2 == null || clave2.isEmpty()) {
            throw new ErrorServicio("La clave del usuario no puede ser nula");
        }
        if (!clave1.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales");
        }
        if (idVivienda == null || idVivienda.isEmpty()) {
            throw new ErrorServicio("La vivienda no puede ser nula");
        } else if (!claveVivienda.equals(viviendaService.buscarPorId(idVivienda).getClaveVivienda())) {
            throw new ErrorServicio("La clave ingresada de la vivienda es incorrecta");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new ErrorServicio("El teléfono no puede ser nulo");
        }
        if (claveVivienda == null || claveVivienda.isEmpty()) {
            throw new ErrorServicio("La clave de la vivienda no puede ser nula");
        }
    }

    @Transactional
    public void darBaja(String mail) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepo.findById(mail);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setActivo(false);
            usuario.setFechaDeModificacion(new Date());
            usuarioRepo.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }

    @Transactional
    public void darAlta(String mail) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepo.findById(mail);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setActivo(true);
            usuarioRepo.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
    }

    public Usuario buscarPorId(String mail) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepo.findById(mail);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            return usuario;
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado");
            //return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findById(mail).get();
        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getTipo().toString());
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            User user = new User(usuario.getMail(), usuario.getClave(), permisos);

            return user;
        } else {
            throw new UsernameNotFoundException("usuario nulo");
        }
    }

    public Usuario getUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogeado = usuarioRepo.getOne(auth.getName());
        return usuarioLogeado;
    }

    public List<Usuario> listarUsuariosComunidad(String mailAdmin) throws ErrorServicio {
        Usuario admin = buscarPorId(mailAdmin);
        List<Usuario> usuarios = usuarioRepo.listarUsuariosComunidad(admin.getVivienda().getComunidad().getId());
        return usuarios;
    }
    
    public List<Usuario> listarUsuarios() throws ErrorServicio {
        List<Usuario> usuarios = usuarioRepo.listarUsuarios();
        return usuarios;
    }
}
