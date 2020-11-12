package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Comunidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComunidadRepo extends JpaRepository<Comunidad, String> {

    @Query("SELECT c FROM Comunidad c WHERE c.activo=true")
    public List<Comunidad> listarComunidades();
    
    @Query("SELECT v.comunidad FROM Vivienda v WHERE v.id LIKE :idVivienda")
    public Comunidad buscarComunidadPorVivienda(@Param("idVivienda")String idVivienda);

    @Query("SELECT c FROM Comunidad c WHERE c.nombre LIKE :nombre")
    public Comunidad buscarPorNombre(@Param("nombre") String nombre);
    
}
