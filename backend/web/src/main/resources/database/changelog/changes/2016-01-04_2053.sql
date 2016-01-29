--liquibase formatted sql
--changeset lautaro:delete_posite_balance splitStatements:false

ALTER TABLE person DROP COLUMN positive_balance;