<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="Sistema de facturación BillinIQ">
        <title>BillinIQ</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <%
            if (session.getAttribute("usuario") == null) {
                response.sendRedirect("index.html");
                return;
            }
        %>

        <header>
            <h1>BillinIQ</h1>
            <img src="${pageContext.request.contextPath}/images/LOGO.png" alt="Logo de la empresa">
        </header>

        <nav>
            <a href="#inicio" onclick="mostrarContenido('inicio')">Inicio</a>
            <a href="#creacion-facturas" onclick="mostrarContenido('creacion-facturas')">Creación de Facturas</a>
            <a href="#informes" onclick="mostrarContenido('informes')">Generación de Informes</a>
            <a href="#gestion-pagos" onclick="mostrarContenido('gestion-pagos')">Gestión de Pagos</a>
            <a href="#sobre-empresa" onclick="mostrarContenido('sobre-empresa')">Sobre la Empresa</a>
            <a href="#registro-cliente" onclick="mostrarContenido('registro-cliente')">Registro Nuevo Cliente</a>
        </nav>

        <section id="contenido">
            <h2>Bienvenido</h2>
            <p>Selecciona una pestaña para ver su contenido.</p>
        </section>

        <section id="creacion-facturas" style="display: none;">
            <h2>Creación de Facturas</h2>
            
            <!-- Buscar Cliente -->
            <label for="buscarCliente">Buscar Cliente:</label>
            <input type="text" id="buscarCliente" name="buscarCliente" oninput="buscarCliente()" aria-required="true" />
            <div id="sugerenciasClientes"></div>

            <!-- Tipo de Cliente -->
            <label for="tipoCliente">Tipo de Cliente:</label>
            <select id="tipoCliente" name="tipoCliente" onchange="mostrarCamposAdicionalesFacturas()">
                <option value="persona-natural">Persona Natural</option>
                <option value="persona-juridica">Persona Jurídica</option>
            </select>

            <!-- Datos del Cliente (rellenados automáticamente) -->
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" readonly required aria-required="true" />

            <label for="direccion">Dirección:</label>
            <input type="text" id="direccion" name="direccion" readonly required aria-required="true" />

            <label for="telefono">Teléfono:</label>
            <input type="tel" id="telefono" name="telefono" readonly required aria-required="true" />

            <label for="correo">Correo Electrónico:</label>
            <input type="email" id="correo" name="correo" readonly required aria-required="true" />

            <label for="numeroCliente">Número de Cliente:</label>
            <input type="text" id="numeroCliente" name="numeroCliente" readonly required aria-required="true" />

            <!-- Campos adicionales para Persona Jurídica -->
            <div id="juridicaCampos" style="display:none;">
                <label for="nit">NIT de la Empresa:</label>
                <input type="text" id="nit" name="nit" readonly aria-required="true" />
            </div>

            <!-- Producto, detalles y precio -->
            <label for="producto">Producto/Servicio:</label>
            <input type="text" id="producto" name="producto" required aria-required="true" />

            <label for="detalles">Detalles:</label>
            <textarea id="detalles" name="detalles" rows="4" required aria-required="true"></textarea>

            <label for="precio">Precio:</label>
            <input type="number" id="precio" name="precio" step="0.01" required aria-required="true" />

            <button type="submit">Generar Factura</button>

            <!-- Factura Generada -->
            <div id="facturaGenerada" style="display:none;">
                <h3>Factura Generada</h3>
                <p id="facturaCliente"></p>
                <p id="facturaProducto"></p>
                <p id="facturaDetalles"></p>
                <p id="facturaPrecio"></p>
            </div>
        </section>

        <section id="registro-cliente" style="display: none;">
            <h2>Registro Nuevo Cliente</h2>
            <form id="registroClienteForm" action="${pageContext.request.contextPath}/RegistroClienteServlet" method="post">
                <label for="nombreRegistro">Nombre:</label>
                <input type="text" id="nombreRegistro" name="nombreRegistro" required aria-required="true" />

                <label for="direccionRegistro">Dirección:</label>
                <input type="text" id="direccionRegistro" name="direccionRegistro" required aria-required="true" />

                <label for="telefonoRegistro">Teléfono:</label>
                <input type="tel" id="telefonoRegistro" name="telefonoRegistro" required aria-required="true" />

                <label for="correoRegistro">Correo Electrónico:</label>
                <input type="email" id="correoRegistro" name="correoRegistro" required aria-required="true" />

                <label for="numeroClienteRegistro">Número de Cliente:</label>
                <input type="text" id="numeroClienteRegistro" name="numeroClienteRegistro" required aria-required="true" />

                <label for="tipoClienteRegistro">Tipo de Cliente:</label>
                <select id="tipoClienteRegistro" name="tipoClienteRegistro" onchange="mostrarCamposAdicionalesRegistro()">
                    <option value="persona-natural">Persona Natural</option>
                    <option value="persona-juridica">Persona Jurídica</option>
                </select>

                <!-- Campos adicionales para Persona Jurídica -->
                <div id="registroJuridicaCampos" style="display:none;">
                    <label for="nitRegistro">NIT de la Empresa:</label>
                    <input type="text" id="nitRegistro" name="nitRegistro" />
                </div>

                <button type="submit">Registrar Cliente</button>
            </form>
        </section>

        <footer>
            <p>&copy; 2024 BillinIQ. Todos los derechos reservados.</p>
        </footer>

        <script>
            // Función para mostrar u ocultar secciones
            function mostrarContenido(id) {
                const secciones = document.querySelectorAll('section');
                secciones.forEach(seccion => {
                    seccion.style.display = seccion.id === id ? 'block' : 'none';
                });
            }

            // Función para buscar clientes
            function buscarCliente() {
                const input = document.getElementById('buscarCliente').value.toLowerCase();
                const sugerencias = document.getElementById('sugerenciasClientes');
                sugerencias.innerHTML = '';

                fetch('${pageContext.request.contextPath}/BuscarClienteServlet?query=' + encodeURIComponent(input))
                    .then(response => response.json())
                    .then(data => {
                        if (data.length > 0) {
                            data.forEach(cliente => {
                                const div = document.createElement('div');
                                div.textContent = cliente.nombre;
                                div.onclick = () => completarFormularioCliente(cliente);
                                sugerencias.appendChild(div);
                            });
                        } else {
                            sugerencias.innerHTML = '<div>No se encontraron clientes.</div>';
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        sugerencias.innerHTML = '<div>Error al buscar clientes.</div>';
                    });
            }

            // Completa el formulario con los datos del cliente seleccionado
            function completarFormularioCliente(cliente) {
                document.getElementById('nombre').value = cliente.nombre;
                document.getElementById('direccion').value = cliente.direccion;
                document.getElementById('telefono').value = cliente.telefono;
                document.getElementById('correo').value = cliente.correo;
                document.getElementById('numeroCliente').value = cliente.numeroCliente;
                document.getElementById('nit').value = cliente.nit || '';
                document.getElementById('juridicaCampos').style.display = cliente.tipoCliente === 'persona-juridica' ? 'block' : 'none';
                document.getElementById('sugerenciasClientes').innerHTML = '';
                document.getElementById('buscarCliente').value = '';
            }

            // Función para mostrar campos adicionales según el tipo de cliente en el registro
            function mostrarCamposAdicionalesRegistro() {
                const tipoCliente = document.getElementById('tipoClienteRegistro').value;
                document.getElementById('registroJuridicaCampos').style.display = tipoCliente === 'persona-juridica' ? 'block' : 'none';
            }

            // Función para mostrar campos adicionales en la creación de facturas
            function mostrarCamposAdicionalesFacturas() {
                const tipoCliente = document.getElementById('tipoCliente').value;
                document.getElementById('juridicaCampos').style.display = tipoCliente === 'persona-juridica' ? 'block' : 'none';
            }
        </script>
    </body>
</html>

