package com.proyecto.tucomunidad.entidades;

import com.proyecto.tucomunidad.Enumeraciones.Rol;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Usuario {

    @Id
    private String mail;
    private String nombre;
    private String apellido;
    private String clave;
    private String idcomunidad;
    @ManyToOne
    private Vivienda vivienda;
    private String telefono;
    @OneToOne
    private Foto foto;
    @Enumerated(EnumType.STRING)
    private Rol tipo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDeAlta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDeModificacion;
    private Boolean activo;

    public Usuario() {
    }

    public Usuario(String mail, String nombre, String clave, Vivienda vivienda, String telefono, Foto foto, Date fechaDeAlta, Date fechaDeModificacion, Boolean activo) {
        this.mail = mail;
        this.nombre = nombre;
        this.clave = clave;
        this.vivienda = vivienda;
        this.telefono = telefono;
        this.foto = foto;
        this.fechaDeAlta = fechaDeAlta;
        this.fechaDeModificacion = fechaDeModificacion;
        this.activo = activo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Vivienda getVivienda() {
        return vivienda;
    }

    public void setVivienda(Vivienda vivienda) {
        this.vivienda = vivienda;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
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

    public Rol getTipo() {
        return tipo;
    }

    public void setTipo(Rol tipo) {
        this.tipo = tipo;
    }

    public String getIdcomunidad() {
        return idcomunidad;
    }

    public void setIdcomunidad(String idcomunidad) {
        this.idcomunidad = idcomunidad;
    }

}
