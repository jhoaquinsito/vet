--liquibase formatted sql
--changeset tomas:person_renspa_set_type_charvar_25 splitStatements:false


ALTER TABLE person
	ALTER COLUMN renspa TYPE character varying(25);