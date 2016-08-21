--liquibase formatted sql
--changeset tomas:legal_person_cuit_to_string splitStatements:false

BEGIN TRANSACTION;

-- elimino la columna de cuit que era numeric
ALTER TABLE legal_person
	DROP cuit;

-- agrego la nueva columna cuit varchar(20)
ALTER TABLE legal_person
   ADD COLUMN cuit character varying(20) NOT NULL;

	
COMMIT;