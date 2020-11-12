package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Oficio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OficioRepo extends JpaRepository <Oficio, String> {
        
    @Query("SELECT o FROM Oficio o WHERE o.activo=true AND o.publicador.vivienda.comunidad.id = :idComunidad")
    public List<Oficio> listarOficiosComunidad(@Param ("idComunidad") String idComunidad);
    
}
