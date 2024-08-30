<%-- 
    Document   : panel
    Created on : 29/08/2024, 7:28:57 p. m.
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css"
            />

    </head>
    <body>
        <%
            if (session.getAttribute("usuario") == null) {
                response.sendRedirect("index.html");
                return;

            }
        %>
        <main class="container-fluid">
        <h1>Bienvenid@</h1>
        </main>
    </body>
    
</html>
