--liquibase formatted sql
--changeset lautaro:settlement_definition splitStatements:false

-- creo la tabla settlement
CREATE TABLE settlement(
	id SERIAL NOT NULL,
	date timestamp without time zone,
	amount NUMERIC(17,4),
	concept VARCHAR(250),
	chek_number NUMERIC(8), -- número de cheque en caso de que el pago se realice con uno
	discounted boolean DEFAULT FALSE,
	person INT,
	last_update_on timestamp without time zone,
	deleted_on timestamp without time zone,
	last_update_user INT,
	CONSTRAINT pk_settlement PRIMARY KEY (id),
	CONSTRAINT pk_person_person FOREIGN KEY (person) REFERENCES person (id)
);