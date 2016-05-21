--liquibase formatted sql
--changeset tomas:natural_person_drop_uk_real_person_document_id splitStatements:false

ALTER TABLE natural_person
  DROP CONSTRAINT uk_real_person_document_id;