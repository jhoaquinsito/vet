--liquibase formatted sql
--changeset lautaro:change_chek_number_type splitStatements:false

ALTER TABLE settlement ALTER COLUMN chek_number TYPE VARCHAR(8);