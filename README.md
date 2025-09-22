Plásticos Perico Verde — Gestión de Entradas y Salidas

Sistema de gestión empresarial desarrollado en Java con APIs REST para administrar clientes, empleados, proveedores y asistencias (horarios), además del control de entradas y salidas de información. Incluye frontend web con HTML, CSS y Bootstrap y persistencia en MySQL.

🚀 Tecnologías principales

Backend: Java (Jersey / JAX-RS), Servlet/JSP, JDBC

REST: Endpoints tipo CRUD (JSON)

BD: MySQL (scripts de creación, vistas, procedimientos e inserts)

Frontend: HTML, CSS, Bootstrap, JavaScript

Servidor: Apache Tomcat (WAR)

IDE recomendado: NetBeans

📦 Módulos del sistema

Usuarios/Login: autenticación básica.

Clientes: registro, edición, consulta y baja.

Proveedores: catálogo y administración.

Empleados: registro, códigos de empleado y contacto.

Asistencias/Horarios: registro de entradas/salidas por fecha.

Inicio (Dashboard simple): acceso a módulos.

🧭 Arquitectura (resumen)

Modelo (POJOs): org.pericoVerde.model.* (Cliente, Empleado, Proveedor, Asistencia, Usuario)

Controladores (DAO/Logica): org.pericoVerde.controller.*

REST (JAX-RS): org.pericoVerde.rest.* (expone JSON)

Persistencia: org.pericoVerde.bd.ConexionMySQL

Web (UI): /web/sistemaPericoVerde/* (HTML + Bootstrap + JS)

🗂️ Estructura relevante del repo
BD/
 ├─ BD FINAL pericoV/
 │   ├─ BDfinalPericoVerde.tsql           # Creación de BD/tablas
 │   ├─ ProcerurePericoVerde.tsql         # Procedimientos almacenados
 │   ├─ VistasPericoVerde.tsql            # Vistas de apoyo
 │   └─ inserts.txt                       # Datos de ejemplo
src/java/org/pericoVerde/
 ├─ bd/ConexionMySQL.java
 ├─ model/ (POJOs)
 ├─ controller/ (lógica/DAO)
 └─ rest/ (REST JAX-RS: Cliente, Empleado, Proveedor, Asistencia, Login)
web/
 ├─ index.html
 ├─ sistemaPericoVerde/
 │   ├─ inicioPericoVerde.html
 │   ├─ moduloClientes/vistaClientes.html
 │   ├─ moduloEmpleados/vistaEmpleado.html
 │   ├─ moduloProveedores/vistaProveedores.html
 │   └─ moduloHorarios/vistaHorarios.html
 ├─ estilos/bootstrap/*                   # Bootstrap local
 └─ js/*.js                               # JS de módulos
dist/PericoVerde.war                      # Artefacto desplegable

🔌 Endpoints REST (ejemplos)

Base sugerida: http://localhost:8080/PericoVerde/api/

Login

POST /login → { usuario, password }

Clientes

GET /clientes — GET /clientes/{id}

POST /clientes — PUT /clientes/{id} — DELETE /clientes/{id}

Proveedores

GET /proveedores ... CRUD completo

Empleados

GET /empleados ... CRUD completo

Asistencias

GET /asistencias?fecha=YYYY-MM-DD

POST /asistencias (codigoEmpleado, fecha, horaEntrada, horaSalida)

Formato: JSON.
Notas: Ajusta la ruta base según tu web.xml y/o Application de JAX-RS.

⚙️ Configuración y despliegue

Base de datos (MySQL)

Crear BD y objetos:

SOURCE "BD/BD FINAL pericoV/BDfinalPericoVerde.tsql";
SOURCE "BD/BD FINAL pericoV/ProcerurePericoVerde.tsql";
SOURCE "BD/BD FINAL pericoV/VistasPericoVerde.tsql";
SOURCE "BD/BD FINAL pericoV/inserts.txt";


Verifica usuario/contraseña y permisos (CREATE, SELECT, INSERT, UPDATE, DELETE).

Conexión a MySQL

Actualiza credenciales en:

src/java/org/pericoVerde/bd/ConexionMySQL.java y/o

web/META-INF/context.xml (si usas DataSource)

Incluye el conector MySQL en WEB-INF/lib (ya se incluye en el repo).

Compilar y ejecutar

Genera el WAR (dist/PericoVerde.war) con NetBeans o Ant.

Despliega en Tomcat: copiar dist/PericoVerde.war a tomcat/webapps/.

Abre: http://localhost:8080/PericoVerde/

🧪 Pruebas rápidas (curl)
# Listar clientes
curl -X GET http://localhost:8080/PericoVerde/api/clientes

# Crear cliente
curl -X POST http://localhost:8080/PericoVerde/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana","apellido_paterno":"López","apellido_materno":"García","genero":"F", ... }'

🖼️ UI (Bootstrap)

Páginas HTML listas en web/sistemaPericoVerde/*

Bootstrap local en web/estilos/bootstrap/

JS por módulo: web/sistemaPericoVerde/js/*.js

Puedes agregar capturas en el README:
![Inicio](web/recursos/pericoVerde.png)
![Clientes](web/recursos/vistaEmp.png)

🔒 Seguridad (nota breve)

Credenciales de BD no deben quedar en el repo para producción.

Considera variables de entorno o JNDI DataSource para despliegues reales.

Habilita CORS y validaciones de entrada si expones el API públicamente.

🗺️ Roadmap corto

 Reportes (PDF/Excel) de clientes, proveedores y asistencias

 Roles y permisos (administrador/operador)

 Filtros avanzados por fecha/rango en asistencias

 Tests de integración para REST

👤 Autor

Yael López Mariano
Proyecto para empresa: Plásticos Perico Verde
Stack: Java, REST, MySQL, HTML, CSS, Bootstrap, JS
