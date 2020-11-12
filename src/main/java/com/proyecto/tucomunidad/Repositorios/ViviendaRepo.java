package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Vivienda;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ViviendaRepo extends JpaRepository<Vivienda, String> {

    @Query("SELECT c FROM Vivienda c WHERE c.activo=true AND c.comunidad.id=:id")
    public List<Vivienda> listarViviendasPorComunidad(@Param("id") String id);

    @Query("Select vot.votantes FROM Proyecto p, IN(p.votacion) vot WHERE p.id LIKE :id")
    public List<Vivienda> listarVotantesPorProyecto(@Param("id") String idProyecto);
    

     @Query("Select u.vivienda FROM Usuario u WHERE u.id LIKE :id")
    public Vivienda obtenerViviendaPorUsuario(@Param("id") String idUsuario);
  
}
