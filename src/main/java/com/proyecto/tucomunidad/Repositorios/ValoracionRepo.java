package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoracionRepo extends JpaRepository <Valoracion, String> {
    
}