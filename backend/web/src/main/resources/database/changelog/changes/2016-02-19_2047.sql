--liquibase formatted sql
--changeset lautaro:inset_categories splitStatements:false

-- Actualizo formato de campo: IVA en tabla de Producto.

ALTER TABLE product ALTER COLUMN iva TYPE numeric(3,1) ;

		
		