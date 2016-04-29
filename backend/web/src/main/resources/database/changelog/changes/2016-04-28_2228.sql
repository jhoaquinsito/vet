--liquibase formatted sql
--changeset tomas:sale_person_set_not_null splitStatements:false

ALTER TABLE sale
ALTER COLUMN person SET NOT NULL;