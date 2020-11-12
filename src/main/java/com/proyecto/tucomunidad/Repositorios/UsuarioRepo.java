package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends JpaRepository <Usuario, String>{
    
    @Query("SELECT u FROM Usuario u WHERE u.activo=true AND u.vivienda.comunidad.id = :idComunidad")
    public List<Usuario> listarUsuariosComunidad(@Param ("idComunidad") String idComunidad);
    
    @Query("SELECT u FROM Usuario u WHERE u.activo=true")
    public List<Usuario> listarUsuarios();
    
}
