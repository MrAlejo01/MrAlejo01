package com.tuyweb.login.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "GenerarFacturaServlet", urlPatterns = {"/generarFactura"})
public class GenerarFacturaServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/servletlogin";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String producto = request.getParameter("producto");
        String detalles = request.getParameter("detalles");
        String precioStr = request.getParameter("precio");

        // Validaciones básicas
        if (nombre == null || producto == null || detalles == null || precioStr == null) {
            response.sendRedirect("error.jsp?error=Datos%20incompletos");
            return;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(precioStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("error.jsp?error=Precio%20inválido");
            return;
        }

        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conexion.prepareStatement("INSERT INTO facturas (nombre, producto, detalles, precio) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, nombre);
            ps.setString(2, producto);
            ps.setString(3, detalles);
            ps.setBigDecimal(4, precio);

            ps.executeUpdate();
            response.sendRedirect("panel.jsp");
        } catch (SQLException e) {
            Logger.getLogger(GenerarFacturaServlet.class.getName()).log(Level.SEVERE, "Error al generar factura", e);
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
        return "Generar Factura Servlet";
    }
}

