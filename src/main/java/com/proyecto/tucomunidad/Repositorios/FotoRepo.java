package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepo extends JpaRepository <Foto, String> {
    
}
