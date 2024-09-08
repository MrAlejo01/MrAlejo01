package com.tuyweb.login.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BuscarClienteServlet", urlPatterns = {"/BuscarClienteServlet"})
public class BuscarClienteServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/servletlogin";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("query");
        if (query == null || query.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Query parameter is missing or empty");
            return;
        }

        List<Cliente> clientes = new ArrayList<>();

        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conexion.prepareStatement("SELECT * FROM clientes WHERE nombre LIKE ?")) {

            ps.setString(1, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setCorreo(rs.getString("correo"));
                    cliente.setNumeroCliente(rs.getString("numeroCliente"));
                    cliente.setTipoCliente(rs.getString("tipoCliente"));

                    if ("persona-juridica".equals(cliente.getTipoCliente())) {
                        cliente.setNit(rs.getString("nit"));
                    }

                    clientes.add(cliente);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the request");
            return;
        }

        // Convertir a JSON y enviar la respuesta
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            out.print(gson.toJson(clientes));
            out.flush();
        }
    }
}
