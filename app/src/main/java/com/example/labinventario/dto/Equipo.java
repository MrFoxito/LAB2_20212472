package com.example.labinventario.dto;

public class Equipo {

    private String codigo;
    private String nombre;
    private String tipoEquipo;
    private String estado;
    private String observaciones;

    public Equipo(String codigo, String nombre, String tipoEquipo, String estado, String observaciones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipoEquipo = tipoEquipo;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
