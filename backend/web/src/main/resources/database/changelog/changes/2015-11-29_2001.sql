--liquibase formatted sql
--changeset lautaro:person_updates splitStatements:false

-- APLICO LAS MODIFICACIONES REFERIDAS A PERSONAS PLANTEADAS PARA EL MODELO v3.0

-- modifico algunos atributos de la tabla person
ALTER TABLE person ALTER COLUMN phone TYPE numeric;
ALTER TABLE person ALTER COLUMN mobile_phone TYPE numeric; 
ALTER TABLE person ADD COLUMN zip_code varchar(8); 

-- modifico el nombre la tabla real_person a natural_person y el atributo document_id a national_id
ALTER TABLE real_person RENAME TO natural_person;
ALTER TABLE natural_person RENAME COLUMN document_id TO national_id; 

-- agrego el porcentaje de IVA a la categoría
ALTER TABLE iva_category ADD COLUMN percentage numeric(4,2);

