--liquibase formatted sql
--changeset tomas:name_unique_constraints splitStatements:false


ALTER TABLE product
	ADD CONSTRAINT uk_product_name UNIQUE (name);

ALTER TABLE category
	ADD CONSTRAINT uk_category_name UNIQUE (name);

ALTER TABLE presentation
	ADD CONSTRAINT uk_presentation_name UNIQUE (name);

ALTER TABLE manufacturer
	ADD CONSTRAINT uk_manufacturer_name UNIQUE (name);

ALTER TABLE measure_unit
	ADD CONSTRAINT uk_measure_unit_name UNIQUE (name);

ALTER TABLE drug
	ADD CONSTRAINT uk_drug_name UNIQUE (name);