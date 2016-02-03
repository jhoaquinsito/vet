--liquibase formatted sql
--changeset lautaro:inset_categories splitStatements:false

-- Inserto las categorías de producto en la base de datos

INSERT INTO category ("name") 
	VALUES ('Medicamentos'),
		('Ropa y calzados'),
		('Talabartería'),
		('Semillas'),
		('Venenos'),
		('Accesorios para mascotas'),
		('Alimentos');
