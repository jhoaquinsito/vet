--liquibase formatted sql
--changeset lautaro:inset_categories splitStatements:false

-- Actualizo formato de campo: IVA en tabla de Producto.

ALTER TABLE settlement RENAME COLUMN chek_number TO check_number

		
