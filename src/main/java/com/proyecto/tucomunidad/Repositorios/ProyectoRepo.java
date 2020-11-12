package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.Enumeraciones.Eleccion;
import com.proyecto.tucomunidad.entidades.Comentario;
import com.proyecto.tucomunidad.entidades.Proyecto;
import com.proyecto.tucomunidad.entidades.Voto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepo extends JpaRepository <Proyecto, String> {
    
    
    @Query("SELECT p FROM Proyecto p WHERE p.comunidad.id = :id and p.activo=true ORDER BY fechaInicio DESC")
    public List<Proyecto> buscarProyectosPorComunidad(@Param ("id") String id);
    
    @Query("SELECT p.comentarios FROM Proyecto p WHERE p.id = :idProyecto")
    public List<Comentario> buscarComentarioPorProyecto(@Param("idProyecto") String id);
    

    @Query("SELECT p.votacion.votos FROM Proyecto p JOIN p.votacion.votos v WHERE p.id = :idProyecto AND v.eleccion  = POSITIVO")
    public List<Voto> buscarVotosAFavorProyecto(@Param("idProyecto") String id);

    
    @Query("SELECT p FROM Proyecto p WHERE p.responsable.mail = :mail and p.activo=true")
    public List<Proyecto> buscarProyectoPorAdmin(@Param("mail") String mail);
    
    @Query("SELECT p FROM Proyecto p JOIN p.votacion.votos v WHERE v.vivienda.id != :idVivienda")
    public List<Proyecto> buscarProyectoPorVivienda(@Param("idVivienda") String idVivienda);

    
}
