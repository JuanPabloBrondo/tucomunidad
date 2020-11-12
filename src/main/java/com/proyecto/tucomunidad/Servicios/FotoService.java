package com.proyecto.tucomunidad.Servicios;

import com.proyecto.tucomunidad.Errores.ErrorServicio;
import com.proyecto.tucomunidad.Repositorios.FotoRepo;
import com.proyecto.tucomunidad.entidades.Foto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoService {

    @Autowired
    private FotoRepo fotoRepositorio;

    //guardar una foto en la base
    @Transactional
    public Foto guardar(MultipartFile archivo) throws ErrorServicio {

        if (archivo != null) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getOriginalFilename());
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        }
        return null;
    }

    //guarda y devuelve una lista de fotos
    @Transactional
    public List<Foto> guardarFotos(List<MultipartFile> archivos) throws ErrorServicio {
        List<Foto> fotos = new ArrayList<>();
        try {
            for (MultipartFile a : archivos) {
                if (a != null) {

                    Foto foto = new Foto();
                    foto.setMime(a.getContentType());
                    foto.setNombre(a.getOriginalFilename());
                    foto.setContenido(a.getBytes());
                    fotoRepositorio.save(foto);
                    fotos.add(foto);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return fotos;
    }

    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio {

        if (archivo != null) {
            try {
                Foto foto = new Foto();

                if (idFoto != null) {
                    Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
                    foto = respuesta.get();
                }
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        }
        return null;
    }

    public Foto buscarFoto(String id) {
        Foto foto = fotoRepositorio.getOne(id);
        return foto;
    }

}
