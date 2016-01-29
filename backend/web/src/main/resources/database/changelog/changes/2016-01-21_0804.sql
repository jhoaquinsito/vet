--liquibase formatted sql
--changeset ggorosito:sale_saleline_fix splitStatements:false


-- Modificaciones que contiene este changeset (en orden):
--- Alteracion de la tabla sale: agregado de DEFAULT para la columna date con el valor de la fecha actual.
--- Alteracion de la tabla saleline: alterado del tipo de dato de la columna discount.

------------------------------------------------
------------------------------------------------

-- Table: saleline
ALTER TABLE sale ALTER COLUMN date  SET DEFAULT current_timestamp ;
ALTER TABLE saleline ALTER COLUMN discount TYPE numeric(4,2) ;
ALTER TABLE saleline ALTER COLUMN discount SET DEFAULT 0.0;