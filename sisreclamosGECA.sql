-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.27-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para sistema_reclamos_geca
CREATE DATABASE IF NOT EXISTS `sistema_reclamos_geca` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `sistema_reclamos_geca`;

-- Volcando estructura para tabla sistema_reclamos_geca.categorias_geca
CREATE TABLE IF NOT EXISTS `categorias_geca` (
  `id_categoria_geca` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_categoria_geca` varchar(100) NOT NULL,
  `descripcion_categoria_geca` text DEFAULT NULL,
  PRIMARY KEY (`id_categoria_geca`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sistema_reclamos_geca.categorias_geca: ~5 rows (aproximadamente)
INSERT INTO `categorias_geca` (`id_categoria_geca`, `nombre_categoria_geca`, `descripcion_categoria_geca`) VALUES
	(1, 'Soporte Técnico', 'Problemas relacionados con hardware y software'),
	(2, 'Facturación', 'Consultas sobre facturas y pagos'),
	(3, 'Servicio al Cliente', 'Atención y calidad de servicio'),
	(4, 'Productos', 'Consultas sobre productos y servicios'),
	(5, 'Otros', 'Otras categorías no especificadas');

-- Volcando estructura para tabla sistema_reclamos_geca.reclamos_geca
CREATE TABLE IF NOT EXISTS `reclamos_geca` (
  `id_reclamo_geca` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario_geca` int(11) NOT NULL,
  `id_categoria_geca` int(11) DEFAULT NULL,
  `titulo_reclamo_geca` varchar(200) NOT NULL,
  `descripcion_reclamo_geca` text NOT NULL,
  `fecha_creacion_geca` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion_geca` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `estado_geca` enum('Pendiente','En atención','Resuelto') DEFAULT 'Pendiente',
  `prioridad_geca` enum('Baja','Media','Alta') DEFAULT 'Media',
  PRIMARY KEY (`id_reclamo_geca`),
  KEY `id_categoria_geca` (`id_categoria_geca`),
  KEY `idx_reclamos_usuario_geca` (`id_usuario_geca`),
  KEY `idx_reclamos_estado_geca` (`estado_geca`),
  CONSTRAINT `reclamos_geca_ibfk_1` FOREIGN KEY (`id_usuario_geca`) REFERENCES `usuarios_geca` (`id_usuario_geca`),
  CONSTRAINT `reclamos_geca_ibfk_2` FOREIGN KEY (`id_categoria_geca`) REFERENCES `categorias_geca` (`id_categoria_geca`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sistema_reclamos_geca.reclamos_geca: ~3 rows (aproximadamente)
INSERT INTO `reclamos_geca` (`id_reclamo_geca`, `id_usuario_geca`, `id_categoria_geca`, `titulo_reclamo_geca`, `descripcion_reclamo_geca`, `fecha_creacion_geca`, `fecha_actualizacion_geca`, `estado_geca`, `prioridad_geca`) VALUES
	(1, 2, 1, 'Problema con internet', 'El servicio de internet está intermitente desde ayer', '2025-10-29 23:55:43', '2025-10-29 23:55:43', 'Pendiente', 'Alta'),
	(2, 3, 2, 'Error en facturación', 'Me cobraron de más en la última factura', '2025-10-29 23:55:43', '2025-10-29 23:55:43', 'En atención', 'Media'),
	(3, 2, 3, 'Mala atención', 'El técnico no se presentó a la cita programada', '2025-10-29 23:55:43', '2025-10-29 23:55:43', 'Resuelto', 'Alta');

-- Volcando estructura para tabla sistema_reclamos_geca.seguimiento_geca
CREATE TABLE IF NOT EXISTS `seguimiento_geca` (
  `id_seguimiento_geca` int(11) NOT NULL AUTO_INCREMENT,
  `id_reclamo_geca` int(11) NOT NULL,
  `id_usuario_geca` int(11) NOT NULL,
  `accion_geca` varchar(200) NOT NULL,
  `observaciones_geca` text DEFAULT NULL,
  `fecha_seguimiento_geca` timestamp NOT NULL DEFAULT current_timestamp(),
  `nuevo_estado_geca` enum('Pendiente','En atención','Resuelto') DEFAULT NULL,
  PRIMARY KEY (`id_seguimiento_geca`),
  KEY `id_usuario_geca` (`id_usuario_geca`),
  KEY `idx_seguimiento_reclamo_geca` (`id_reclamo_geca`),
  CONSTRAINT `seguimiento_geca_ibfk_1` FOREIGN KEY (`id_reclamo_geca`) REFERENCES `reclamos_geca` (`id_reclamo_geca`),
  CONSTRAINT `seguimiento_geca_ibfk_2` FOREIGN KEY (`id_usuario_geca`) REFERENCES `usuarios_geca` (`id_usuario_geca`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sistema_reclamos_geca.seguimiento_geca: ~3 rows (aproximadamente)
INSERT INTO `seguimiento_geca` (`id_seguimiento_geca`, `id_reclamo_geca`, `id_usuario_geca`, `accion_geca`, `observaciones_geca`, `fecha_seguimiento_geca`, `nuevo_estado_geca`) VALUES
	(1, 1, 2, 'Reclamo registrado', 'Usuario reporta problema con internet', '2025-10-29 23:55:43', 'Pendiente'),
	(2, 2, 1, 'Reclamo asignado', 'El reclamo ha sido asignado al área de facturación', '2025-10-29 23:55:43', 'En atención'),
	(3, 3, 1, 'Reclamo resuelto', 'Se programó nueva cita y se compensó al usuario', '2025-10-29 23:55:43', 'Resuelto');

-- Volcando estructura para tabla sistema_reclamos_geca.usuarios_geca
CREATE TABLE IF NOT EXISTS `usuarios_geca` (
  `id_usuario_geca` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_geca` varchar(100) NOT NULL,
  `email_geca` varchar(150) NOT NULL,
  `password_geca` varchar(255) NOT NULL,
  `rol_geca` enum('Usuario','Administrador') DEFAULT 'Usuario',
  `ip_permitida_geca` varchar(45) DEFAULT NULL,
  `fecha_registro_geca` timestamp NOT NULL DEFAULT current_timestamp(),
  `estado_geca` enum('Activo','Inactivo') DEFAULT 'Activo',
  PRIMARY KEY (`id_usuario_geca`),
  UNIQUE KEY `email_geca` (`email_geca`),
  KEY `idx_usuario_email_geca` (`email_geca`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla sistema_reclamos_geca.usuarios_geca: ~3 rows (aproximadamente)
INSERT INTO `usuarios_geca` (`id_usuario_geca`, `nombre_geca`, `email_geca`, `password_geca`, `rol_geca`, `ip_permitida_geca`, `fecha_registro_geca`, `estado_geca`) VALUES
	(1, 'Admin GECA', 'admin@geca.com', 'admin123', 'Administrador', '192.168.1.100', '2025-10-29 23:55:43', 'Activo'),
	(2, 'Usuario Demo', 'usuario@geca.com', 'user123', 'Usuario', '192.168.1.101', '2025-10-29 23:55:43', 'Activo'),
	(3, 'Carlos Perez', 'carlos@geca.com', 'pass123', 'Usuario', '192.168.1.102', '2025-10-29 23:55:43', 'Activo');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
