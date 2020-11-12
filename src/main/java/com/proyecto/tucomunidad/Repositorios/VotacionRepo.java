package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Proyecto;
import com.proyecto.tucomunidad.entidades.Vivienda;
import com.proyecto.tucomunidad.entidades.Votacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotacionRepo extends JpaRepository <Votacion, String> {

    
}
