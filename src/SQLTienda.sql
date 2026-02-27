DROP DATABASE IF EXISTS tienda;
CREATE DATABASE tienda CHARACTER SET utf8mb4;
USE tienda;

-- TABLAS

CREATE TABLE fabricante (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL
);

CREATE TABLE producto (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  stock INT UNSIGNED NOT NULL DEFAULT 0,
  id_fabricante INT UNSIGNED NOT NULL,
  FOREIGN KEY (id_fabricante)
    REFERENCES fabricante(id)
    ON UPDATE CASCADE
);

CREATE TABLE cliente (
  id_cliente INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  telefono VARCHAR(20),
  direccion TEXT,
  activo TINYINT DEFAULT 1 COMMENT '1=Activo, 0=Inactivo'
);

CREATE TABLE compra (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_cliente INT UNSIGNED NOT NULL,
  fecha_transaccion DATETIME DEFAULT CURRENT_TIMESTAMP,
  total_compra DECIMAL(10,2) DEFAULT 0,
  FOREIGN KEY (id_cliente)
    REFERENCES cliente(id_cliente)
    ON UPDATE CASCADE
);

CREATE TABLE compra_detalle (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_compra INT UNSIGNED NOT NULL,
  id_producto INT UNSIGNED NOT NULL,
  cantidad INT NOT NULL DEFAULT 1,
  precio_unitario DECIMAL(10,2),
  FOREIGN KEY (id_compra)
    REFERENCES compra(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (id_producto)
    REFERENCES producto(id)
    ON UPDATE CASCADE
);

-- TRIGGERS

DELIMITER //

CREATE TRIGGER tg_copiar_precio_producto
BEFORE INSERT ON compra_detalle
FOR EACH ROW
BEGIN
  IF NEW.precio_unitario IS NULL THEN
    SET NEW.precio_unitario = (
      SELECT precio FROM producto WHERE id = NEW.id_producto
    );
  END IF;
END//

CREATE TRIGGER tg_total_insert
AFTER INSERT ON compra_detalle
FOR EACH ROW
BEGIN
  UPDATE compra
  SET total_compra = total_compra + (NEW.cantidad * NEW.precio_unitario)
  WHERE id = NEW.id_compra;
END//

CREATE TRIGGER tg_total_update
AFTER UPDATE ON compra_detalle
FOR EACH ROW
BEGIN
  IF OLD.id_compra = NEW.id_compra THEN
    UPDATE compra
    SET total_compra = total_compra
      - (OLD.cantidad * OLD.precio_unitario)
      + (NEW.cantidad * NEW.precio_unitario)
    WHERE id = NEW.id_compra;
  ELSE
    UPDATE compra
    SET total_compra = total_compra - (OLD.cantidad * OLD.precio_unitario)
    WHERE id = OLD.id_compra;

    UPDATE compra
    SET total_compra = total_compra + (NEW.cantidad * NEW.precio_unitario)
    WHERE id = NEW.id_compra;
  END IF;
END//

CREATE TRIGGER tg_total_delete
AFTER DELETE ON compra_detalle
FOR EACH ROW
BEGIN
  UPDATE compra
  SET total_compra = total_compra - (OLD.cantidad * OLD.precio_unitario)
  WHERE id = OLD.id_compra;
END//

CREATE TRIGGER tg_stock_insert
BEFORE INSERT ON compra_detalle
FOR EACH ROW
BEGIN
  DECLARE stock_actual INT;

  SELECT stock INTO stock_actual
  FROM producto
  WHERE id = NEW.id_producto;

  IF stock_actual < NEW.cantidad THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Stock insuficiente para el producto';
  END IF;

  UPDATE producto
  SET stock = stock - NEW.cantidad
  WHERE id = NEW.id_producto;
END//

CREATE TRIGGER tg_stock_update
BEFORE UPDATE ON compra_detalle
FOR EACH ROW
BEGIN
  DECLARE diferencia INT;

  SET diferencia = NEW.cantidad - OLD.cantidad;

  IF diferencia <> 0 THEN
    IF (SELECT stock FROM producto WHERE id = NEW.id_producto) < diferencia THEN
      SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente para actualizar la cantidad';
    END IF;

    UPDATE producto
    SET stock = stock - diferencia
    WHERE id = NEW.id_producto;
  END IF;
END//

CREATE TRIGGER tg_stock_delete
AFTER DELETE ON compra_detalle
FOR EACH ROW
BEGIN
  UPDATE producto
  SET stock = stock + OLD.cantidad
  WHERE id = OLD.id_producto;
END//

DELIMITER ;

-- INSERTS

INSERT INTO fabricante (id, nombre) VALUES
(1, 'Asus'),
(2, 'Lenovo'),
(3, 'Hewlett-Packard'),
(4, 'Samsung'),
(5, 'Seagate'),
(6, 'Crucial'),
(7, 'Gigabyte'),
(8, 'Huawei'),
(9, 'Xiaomi');

INSERT INTO producto (id, nombre, precio, stock, id_fabricante) VALUES
(1, 'Disco duro SATA3 1TB', 86.99, 50, 5),
(2, 'Memoria RAM DDR4 8GB', 120.00, 40, 6),
(3, 'Disco SSD 1 TB', 150.99, 30, 4),
(4, 'GeForce GTX 1050Ti', 185.00, 20, 7),
(5, 'GeForce GTX 1080 Xtreme', 755.00, 10, 7),
(6, 'Monitor 24 LED Full HD', 202.00, 25, 1),
(7, 'Monitor 27 LED Full HD', 245.99, 15, 1),
(8, 'Portátil Yoga 520', 559.00, 12, 2),
(9, 'Portátil Ideapad 320', 444.00, 18, 2),
(10, 'Impresora HP Deskjet 3720', 59.99, 35, 3),
(11, 'Impresora HP Laserjet Pro M26nw', 180.00, 22, 3);

INSERT INTO cliente (nombre, email, telefono, direccion, activo) VALUES
('Ana García', 'ana.garcia@email.com', '+34 600 111 222', 'Calle Mayor 15, Madrid', 1),
('Juan Pérez', 'jperez@servidor.es', '912 344 555', 'Av. de la Constitución 40, Sevilla', 1),
('Marta Sánchez', 'marta.sanchez@pyme.com', '+34 677 888 999', 'Plaza de España 5, Barcelona', 1),
('Roberto Gómez', 'roberto.g@freelance.org', '600 000 111', 'Calle Silencio s/n, Teruel', 0),
('Lucía Torres', 'l.torres@academia.edu', '+34 933 445 566', 'Via Augusta 120, Barcelona', 1),
('Diego Rivera', 'drivera@arte.mx', '555-0199', 'Barrio del Artista 12, Puebla', 1);

INSERT INTO compra (id_cliente) VALUES
(1),
(2),
(3);

INSERT INTO compra_detalle (id_compra, id_producto, cantidad) VALUES
(1, 1, 2),
(1, 2, 1),
(1, 6, 1);

INSERT INTO compra_detalle (id_compra, id_producto, cantidad) VALUES
(2, 8, 1),
(2, 10, 1);

INSERT INTO compra_detalle (id_compra, id_producto, cantidad) VALUES
(3, 3, 1),
(3, 4, 1),
(3, 2, 2);
