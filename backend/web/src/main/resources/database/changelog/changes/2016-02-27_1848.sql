--liquibase formatted sql
--changeset ggorosito splitStatements:false

-- Agrego campo de abreviaturas en la tabla de Unidad de Medidas

ALTER TABLE measure_unit
ADD COLUMN  abbreviation character varying(5) ;
		
-- Actualizo los valores actuales:

UPDATE measure_unit SET abbreviation = 'Kg.'  WHERE id = 1;
UPDATE measure_unit SET abbreviation = 'Lt.'  WHERE id = 2;
UPDATE measure_unit SET abbreviation = 'U.'   WHERE id = 3;
UPDATE measure_unit SET abbreviation = 'N/E.' WHERE id = 4;			