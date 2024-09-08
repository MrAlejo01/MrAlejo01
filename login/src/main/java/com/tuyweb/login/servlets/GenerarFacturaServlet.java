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
import java.sql.ResultSet;
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

        // Verificar que el cliente existe en la base de datos
        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement psCliente = conexion.prepareStatement("SELECT * FROM clientes WHERE nombre = ?");
             PreparedStatement psFactura = conexion.prepareStatement("INSERT INTO facturas (nombre, producto, detalles, precio) VALUES (?, ?, ?, ?)")) {

            psCliente.setString(1, nombre);
            ResultSet rsCliente = psCliente.executeQuery();

            if (!rsCliente.next()) {
                response.sendRedirect("error.jsp?error=Cliente%20no%20encontrado");
                return;
            }

            // Insertar la factura en la base de datos
            psFactura.setString(1, nombre);
            psFactura.setString(2, producto);
            psFactura.setString(3, detalles);
            psFactura.setBigDecimal(4, precio);

            psFactura.executeUpdate();
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
