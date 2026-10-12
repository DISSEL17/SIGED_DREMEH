# SIGED DREMEH - APF2 (JSF + Bootstrap)

Continuacion del APF1. Stack: Jakarta Faces 4.0 (Mojarra), CDI (Weld), Bootstrap 5, JDBC/DAO, MySQL, Tomcat 10.1, Java 17.

## Puesta en marcha
1. Ejecutar `sql/siged_dremeh.sql` en MySQL Workbench (crea BD, usuario `siged`, tablas y datos ficticios).
2. Revisar `src/main/resources/db.properties`.
3. Abrir en NetBeans 17+ (proyecto Maven), agregar Tomcat 10.1 y ejecutar (Run).
4. Entrar a `http://localhost:8080/siged-dremeh/` con: tu contraseña xd

## Paquetes (MVC adaptado a JSF)
- `model`   : DTO (Usuario, Empleado, Documento, Historial)
- `dao`     : acceso a datos JDBC
- `bean`    : Managed Beans CDI (@Named + ambitos)
- `util`    : Conexion, PasswordUtil
- `filter`  : AuthFilter
- `webapp`  : vistas XHTML/Facelets, plantilla y CSS
