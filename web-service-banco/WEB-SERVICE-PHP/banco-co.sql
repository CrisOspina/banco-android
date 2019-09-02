-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 02-09-2019 a las 06:36:28
-- Versión del servidor: 10.1.32-MariaDB
-- Versión de PHP: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `banco-co`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `ident` bigint(12) NOT NULL,
  `nombres` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `clave` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`ident`, `nombres`, `email`, `clave`) VALUES
(111, 'cris', 'cris@gmail.com', '123'),
(222, 'prueba', 'prueba@gmail.com', '123'),
(666, 'hel', 'hel@gmail.com', '123'),
(101010, 'Hades', 'hades@gmail.com', '123');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuenta`
--

CREATE TABLE `cuenta` (
  `nrocuenta` int(11) NOT NULL,
  `ident` bigint(12) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `saldo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cuenta`
--

INSERT INTO `cuenta` (`nrocuenta`, `ident`, `fecha`, `saldo`) VALUES
(101, 10101010, '2019-09-02 04:33:10', 0),
(999, 555, '0000-00-00 00:00:00', 0),
(3344, 111, '0000-00-00 00:00:00', 20000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transaccion`
--

CREATE TABLE `transaccion` (
  `nrotransaccion` int(11) NOT NULL,
  `nrocuentaorigen` int(11) NOT NULL,
  `nrocuentadestino` varchar(11) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `valor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `transaccion`
--

INSERT INTO `transaccion` (`nrotransaccion`, `nrocuentaorigen`, `nrocuentadestino`, `fecha`, `valor`) VALUES
(1, 3344, '999', '0000-00-00 00:00:00', 10000),
(2, 3344, '999', '0000-00-00 00:00:00', 40000),
(3, 3344, '999', '0000-00-00 00:00:00', 2000),
(4, 3344, '999', '0000-00-00 00:00:00', 30000000),
(5, 3344, '999', '0000-00-00 00:00:00', 4500000),
(6, 3344, '999', '2019-09-02 03:09:18', 900),
(7, 3344, '999', '2019-09-02 03:52:42', 10000),
(8, 3344, '101', '2019-09-02 04:33:55', 10000);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`ident`);

--
-- Indices de la tabla `cuenta`
--
ALTER TABLE `cuenta`
  ADD PRIMARY KEY (`nrocuenta`),
  ADD KEY `ident` (`ident`) USING BTREE;

--
-- Indices de la tabla `transaccion`
--
ALTER TABLE `transaccion`
  ADD PRIMARY KEY (`nrotransaccion`),
  ADD KEY `nrocuentaorigen` (`nrocuentaorigen`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `transaccion`
--
ALTER TABLE `transaccion`
  MODIFY `nrotransaccion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `transaccion`
--
ALTER TABLE `transaccion`
  ADD CONSTRAINT `transaccion_ibfk_1` FOREIGN KEY (`nrocuentaorigen`) REFERENCES `cuenta` (`nrocuenta`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
