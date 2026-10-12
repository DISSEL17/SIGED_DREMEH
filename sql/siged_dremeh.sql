-- SIGED DREMEH - Base de datos (APF2). Datos de prueba ficticios.
CREATE DATABASE IF NOT EXISTS siged_dremeh CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE siged_dremeh;

CREATE USER IF NOT EXISTS 'siged'@'localhost' IDENTIFIED BY 'siged123';
GRANT ALL PRIVILEGES ON siged_dremeh.* TO 'siged'@'localhost';

DROP TABLE IF EXISTS historial;
DROP TABLE IF EXISTS derivacion;
DROP TABLE IF EXISTS documento;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS empleado;

CREATE TABLE empleado (
  id_empleado INT AUTO_INCREMENT PRIMARY KEY,
  dni         CHAR(8) NOT NULL UNIQUE,
  nombres     VARCHAR(60) NOT NULL,
  apellidos   VARCHAR(60) NOT NULL,
  cargo       VARCHAR(60) NOT NULL,
  area        VARCHAR(60) NOT NULL,
  estado      TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE usuario (
  id_usuario     INT AUTO_INCREMENT PRIMARY KEY,
  nombre_usuario VARCHAR(30) NOT NULL UNIQUE,
  hash_clave     VARCHAR(120) NOT NULL,
  rol            ENUM('ADMINISTRADOR','MESA_PARTES','RESPONSABLE','SUPERVISOR') NOT NULL,
  id_empleado    INT NULL,
  estado         TINYINT(1) NOT NULL DEFAULT 1,
  FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

CREATE TABLE documento (
  id_documento  INT AUTO_INCREMENT PRIMARY KEY,
  expediente    VARCHAR(20) NOT NULL UNIQUE,
  fecha_ingreso DATE NOT NULL,
  tipo          VARCHAR(30) NOT NULL,
  asunto        VARCHAR(200) NOT NULL,
  solicitante   VARCHAR(100) NOT NULL,
  folios        INT NOT NULL DEFAULT 1,
  estado        ENUM('REGISTRADO','DERIVADO','EN_ATENCION','ATENDIDO','CERRADO') NOT NULL DEFAULT 'REGISTRADO'
);

CREATE TABLE derivacion (
  id_derivacion INT AUTO_INCREMENT PRIMARY KEY,
  id_documento  INT NOT NULL,
  id_empleado   INT NOT NULL,
  fecha         DATETIME NOT NULL,
  observacion   VARCHAR(250),
  FOREIGN KEY (id_documento) REFERENCES documento(id_documento),
  FOREIGN KEY (id_empleado)  REFERENCES empleado(id_empleado)
);

CREATE TABLE historial (
  id_evento       INT AUTO_INCREMENT PRIMARY KEY,
  id_documento    INT NOT NULL,
  id_usuario      INT NOT NULL,
  fecha           DATETIME NOT NULL,
  accion          VARCHAR(80) NOT NULL,
  estado_anterior VARCHAR(20),
  estado_nuevo    VARCHAR(20) NOT NULL,
  FOREIGN KEY (id_documento) REFERENCES documento(id_documento),
  FOREIGN KEY (id_usuario)   REFERENCES usuario(id_usuario)
);

INSERT INTO empleado (dni,nombres,apellidos,cargo,area) VALUES
 ('10000001','Rosa','Mendoza Vega','Tecnica de mesa de partes','Mesa de Partes'),
 ('10000002','Luis','Paredes Rojas','Ingeniero de energia','Direccion de Energia'),
 ('10000003','Carmen','Salas Ortiz','Ingeniera de minas','Direccion de Minas'),
 ('10000004','Jorge','Ramos Leon','Especialista en hidrocarburos','Direccion de Hidrocarburos'),
 ('10000005','Ana','Torres Cano','Administradora del sistema','Oficina de Administracion');

INSERT INTO usuario (nombre_usuario,hash_clave,rol,id_empleado) VALUES
 ('admin','a1b2c3d4e5f60718293a4b5c6d7e8f90$1714dc8a640928d708c3651c2693ff4320b4ea94729cab59a077d2dad101901b','ADMINISTRADOR',5),
 ('mesa','0f1e2d3c4b5a69788796a5b4c3d2e1f0$af5b7aaf9613a9b3879693af140457b4dbc2414562a2d8d628556b11c8687e05','MESA_PARTES',1),
 ('resp','11223344556677889900aabbccddeeff$e722032e33c76a153ffd37a60c6510963059622961af59fe0c7eac8967fad669','RESPONSABLE',2);

INSERT INTO documento (expediente,fecha_ingreso,tipo,asunto,solicitante,folios,estado) VALUES
 ('EXP-2026-0001','2026-09-14','Solicitud','Solicitud de informacion sobre concesion minera','Empresa Demo SAC',5,'REGISTRADO'),
 ('EXP-2026-0002','2026-09-15','Oficio','Oficio sobre electrificacion rural en caserio ficticio','Municipalidad Ejemplo',3,'DERIVADO'),
 ('EXP-2026-0003','2026-09-16','Carta','Carta de consulta sobre autorizacion de grifo','Ciudadano de Prueba',2,'EN_ATENCION'),
 ('EXP-2026-0004','2026-09-18','Solicitud','Solicitud de inspeccion a planta de gas','Comite Vecinal Demo',4,'ATENDIDO'),
 ('EXP-2026-0005','2026-09-21','Informe','Informe tecnico de actividad minera artesanal','Asociacion Demo',8,'CERRADO'),
 ('EXP-2026-0006','2026-09-22','Solicitud','Solicitud de constancia de no adeudo','Persona Natural Demo',1,'REGISTRADO'),
 ('EXP-2026-0007','2026-09-24','Oficio','Oficio de coordinacion con el Gobierno Regional','Area interna demo',2,'DERIVADO');

INSERT INTO derivacion (id_documento,id_empleado,fecha,observacion) VALUES
 (2,2,'2026-09-15 11:00:00','Atender segun competencia de energia'),
 (3,4,'2026-09-16 15:30:00','Revisar autorizacion de grifo'),
 (7,3,'2026-09-24 09:20:00','Coordinar respuesta');

INSERT INTO historial (id_documento,id_usuario,fecha,accion,estado_anterior,estado_nuevo) VALUES
 (1,2,'2026-09-14 09:00:00','Registro de documento',NULL,'REGISTRADO'),
 (2,2,'2026-09-15 10:40:00','Registro de documento',NULL,'REGISTRADO'),
 (2,2,'2026-09-15 11:00:00','Derivacion a Luis Paredes Rojas','REGISTRADO','DERIVADO'),
 (3,2,'2026-09-16 15:00:00','Registro de documento',NULL,'REGISTRADO'),
 (3,2,'2026-09-16 15:30:00','Derivacion a Jorge Ramos Leon','REGISTRADO','DERIVADO'),
 (3,3,'2026-09-17 08:45:00','Cambio de estado','DERIVADO','EN_ATENCION');
