
package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Hito;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HitoRepo extends JpaRepository <Hito, String> {
    
    @Query("SELECT h FROM Hito h WHERE h.activo=true AND h.comunidad.id = :idComunidad")
    public List<Hito> listarHitosComunidad(@Param ("idComunidad") String idComunidad);
    
}
