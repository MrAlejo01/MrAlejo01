package com.tuyweb.login.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/servletlogin";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String contraseña = request.getParameter("contraseña");

        String query = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = conexion.prepareStatement(query)) {

                ps.setString(1, usuario);
                ps.setString(2, contraseña);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        request.getSession().setAttribute("usuario", usuario);
                        response.sendRedirect("panel.jsp");
                    } else {
                        response.sendRedirect("index.html?error=invalid_credentials");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, e);
            response.sendRedirect("index.html?error=server_error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Login Servlet";
    }
}
