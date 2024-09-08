package com.tuyweb.login.servlets;

public class Cliente {
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private String numeroCliente;
    private String tipoCliente;
    private String nit; // Solo para persona jurídica

    // Constructor vacío
    public Cliente() {
    }

    // Constructor con todos los campos
    public Cliente(String nombre, String direccion, String telefono, String correo, String numeroCliente, String tipoCliente, String nit) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.numeroCliente = numeroCliente;
        this.tipoCliente = tipoCliente;
        this.nit = nit;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(String numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", numeroCliente='" + numeroCliente + '\'' +
                ", tipoCliente='" + tipoCliente + '\'' +
                ", nit='" + nit + '\'' +
                '}';
    }
}
