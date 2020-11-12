package com.proyecto.tucomunidad.controladores;

import com.proyecto.tucomunidad.Servicios.FotoService;
import com.proyecto.tucomunidad.entidades.Foto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class fotoControlador {

    @Autowired
    private FotoService fotoServicio;

    @GetMapping("/load/{id}")
    public ResponseEntity<byte[]> foto(@PathVariable String id, ModelMap modelo) {
        Foto foto = null;
        try {
            foto = fotoServicio.buscarFoto(id);
            modelo.put("nombre", foto.getNombre());

        } catch (Exception e) {
            modelo.put("error", e);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(foto.getContenido(), headers, HttpStatus.OK);
    }
}
