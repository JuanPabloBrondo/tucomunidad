package com.proyecto.tucomunidad.entidades;

import com.proyecto.tucomunidad.Enumeraciones.Categoria;
import java.util.Date;
import java.util.List;
import static javassist.Modifier.ENUM;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Oficio {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    
    private String nombre; 
    
    private String contacto; //teléfono, domicilio contacto, etc.
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDeAlta;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDeModificacion;
    
    private Boolean activo;

    @OneToMany
    private List<Valoracion> valoraciones;

    @ManyToOne
    private Usuario publicador;

    public Oficio() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    public Usuario getPublicador() {
        return publicador;
    }

    public void setPublicador(Usuario publicador) {
        this.publicador = publicador;
    }

    public Date getFechaDeAlta() {
        return fechaDeAlta;
    }

    public void setFechaDeAlta(Date fechaDeAlta) {
        this.fechaDeAlta = fechaDeAlta;
    }

    public Date getFechaDeModificacion() {
        return fechaDeModificacion;
    }

    public void setFechaDeModificacion(Date fechaDeModificacion) {
        this.fechaDeModificacion = fechaDeModificacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Oficios{" + "id=" + id + ", categoria=" + categoria + ", contacto=" + contacto + ", valoraciones=" + valoraciones + ", publicador=" + publicador + '}';
    }

}
