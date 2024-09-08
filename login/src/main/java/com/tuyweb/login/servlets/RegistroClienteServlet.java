package com.tuyweb.login.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "RegistroClienteServlet", urlPatterns = {"/registroCliente"})
public class RegistroClienteServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/servletlogin";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombreRegistro");
        String direccion = request.getParameter("direccionRegistro");
        String telefono = request.getParameter("telefonoRegistro");
        String correo = request.getParameter("correoRegistro");
        String numeroCliente = request.getParameter("numeroClienteRegistro");
        String tipoCliente = request.getParameter("tipoClienteRegistro");
        String nit = request.getParameter("nitRegistro");

        // Validaciones básicas
        if (nombre == null || direccion == null || telefono == null || correo == null || numeroCliente == null || tipoCliente == null) {
            response.sendRedirect("error.jsp?error=Datos%20incompletos");
            return;
        }

        // Inicializar la conexión y el PreparedStatement
        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement ps = conexion.prepareStatement("INSERT INTO clientes (nombre, direccion, telefono, correo, numeroCliente, tipoCliente, nit) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            ps.setString(5, numeroCliente);
            ps.setString(6, tipoCliente);
            ps.setString(7, nit != null ? nit : "");  // Usar una cadena vacía si nit es null

            ps.executeUpdate();
            response.sendRedirect("panel.jsp");
        } catch (SQLException e) {
            Logger.getLogger(RegistroClienteServlet.class.getName()).log(Level.SEVERE, "Error al registrar cliente", e);
            response.sendRedirect("error.jsp?error=Error%20en%20la%20base%20de%20datos");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Registro de Clientes Servlet";
    }
}
