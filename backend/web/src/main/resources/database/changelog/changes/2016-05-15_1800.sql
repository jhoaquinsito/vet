--liquibase formatted sql
--changeset tomas:add_missing_triggers splitStatements:false

ALTER TABLE city
	ALTER COLUMN last_update_user TYPE character varying(30);

ALTER TABLE legal_person
	ALTER COLUMN last_update_user TYPE character varying(30);

ALTER TABLE natural_person
	ALTER COLUMN last_update_user TYPE character varying(30);

ALTER TABLE person
	ALTER COLUMN last_update_user TYPE character varying(30);

ALTER TABLE settlement
	ALTER COLUMN last_update_user TYPE character varying(30);

ALTER TABLE state
	ALTER COLUMN last_update_user TYPE character varying(30);