--liquibase formatted sql
--changeset tomas:legal_person_drop_uk_legal_person_cuit splitStatements:false

ALTER TABLE legal_person
  DROP CONSTRAINT uk_legal_person_cuit;