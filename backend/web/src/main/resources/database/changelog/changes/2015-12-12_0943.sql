--liquibase formatted sql
--changeset ggorosito:person_implementation splitStatements:false


-- IMPLEMENTACIÓN DE MEJORA: - Campo apellido en tabla de Personas.

-- Tablas que contiene este changeset (en orden):
-- Alter table de person: Se agrega el LASTNAME

------------------------------------------------
------------------------------------------------
-- Table: person

-- DROP TABLE person;

	ALTER TABLE person
	ADD COLUMN lastname character varying(30) NOT NULL
	DEFAULT 'TODO-APELLIDO';
	
	ALTER TABLE person
	ADD COLUMN active boolean NOT NULL
	DEFAULT true;
