--liquibase formatted sql
--changeset ggorosito:sale_implementation splitStatements:false


-- Modificaciones que contiene este changeset (en orden):
--- Alteracion de la tabla saleline: nueva columna.

------------------------------------------------
------------------------------------------------

-- Table: saleline

ALTER TABLE saleline
ADD COLUMN discount numeric(17,4) DEFAULT 0.0
  