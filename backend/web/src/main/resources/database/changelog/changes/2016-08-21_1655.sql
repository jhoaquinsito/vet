--liquibase formatted sql
--changeset tomas:legal_person_cuit_to_string splitStatements:false

BEGIN TRANSACTION;

-- cambio la columna de cuit a string (era numeric)
ALTER TABLE legal_person 
	ALTER COLUMN cuit TYPE character varying(20);

	
COMMIT;