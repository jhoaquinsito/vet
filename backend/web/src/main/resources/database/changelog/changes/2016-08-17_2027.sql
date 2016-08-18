--liquibase formatted sql
--changeset lautaro:add_form_of_sale_table splitStatements:false

BEGIN TRANSACTION;
-- creo la tabla para almacenar las formas de venta
CREATE TABLE form_of_sale (
	id SERIAL NOT NULL,
	description VARCHAR(100) NOT NULL,
	CONSTRAINT pk_form_of_sale PRIMARY KEY(id)
);

-- inserto las formas de venta válidas actualmente
INSERT INTO form_of_sale(description)
	VALUES ('Contado'),
			('Cuenta corriente');

-- agrego la clave foranea en la tabla sale
ALTER TABLE sale ADD COLUMN form_of_sale INT;
ALTER TABLE sale ADD CONSTRAINT fk_form_of_sale_form_of_sale
	FOREIGN KEY (form_of_sale) REFERENCES form_of_sale;
	
COMMIT;