package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepo extends JpaRepository <Voto, String> {
    
}
