package com.proyecto.tucomunidad.Repositorios;

import com.proyecto.tucomunidad.entidades.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepo extends JpaRepository<Comentario, String>{
    
    
}
