--liquibase formatted sql
--changeset lautaro:phone_numbers_type splitStatements:false

-- cambio el tipo de datos de las columnas phone y mobile_phone de la tabla person
ALTER TABLE person ALTER COLUMN phone TYPE VARCHAR(50);
ALTER TABLE person ALTER COLUMN mobile_phone TYPE VARCHAR(50);
