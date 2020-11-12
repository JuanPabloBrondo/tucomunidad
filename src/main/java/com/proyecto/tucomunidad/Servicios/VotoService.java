package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Enumeraciones.Eleccion;
import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.ViviendaRepo;
import com.proyecto.tucomunidad.Repositorios.VotoRepo;
import com.proyecto.tucomunidad.entidades.Vivienda;
import com.proyecto.tucomunidad.entidades.Voto;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {
    
    @Autowired
    private VotoRepo votoRepo;
    @Autowired
    private ViviendaService viviendaService;
    
    @Autowired
    private ViviendaRepo viviendaRepo;
    @Transactional
    public Voto registrarVoto(Eleccion eleccion,String idVivienda)throws ErrorServicio{
        Voto voto = new Voto();
        validar(eleccion, idVivienda);
        voto.setEleccion(eleccion);
        Vivienda vivienda=viviendaService.buscarPorId(idVivienda);
        Optional<Vivienda> respuesta = viviendaRepo.findById(idVivienda);
        if (respuesta.isPresent()) {
            Vivienda vivienda1 = respuesta.get();
            System.out.println("La vivienda si existe. **************** "+ vivienda1.getId());
        }
        voto.setVivienda(vivienda);
        voto.setFechaVoto(new Date());
        votoRepo.save(voto);
 
        return voto;
    }
    
    
    private void validar(Eleccion eleccion,String idVivienda)throws ErrorServicio{
        if(eleccion==null ){
            throw new ErrorServicio("La eleccion no puede ser nula");
            
        }
         if (idVivienda == null || idVivienda.isEmpty()) {
            throw new ErrorServicio("El id de la vivienda no puede ser nulo");
        }
    }
    
    
    
    
    
    
}
