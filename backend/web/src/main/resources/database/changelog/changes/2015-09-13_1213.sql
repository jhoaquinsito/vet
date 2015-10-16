--liquibase formatted sql
--changeset tomas:initial_definition splitStatements:false


-- DEFINICIÓN INICIAL DE LA BASE DE DATOS

------------------------------------------------
------------------------------------------------
-- Table: category

-- DROP TABLE category;

CREATE TABLE category
(
  id serial NOT NULL,
  name character varying(30) NOT NULL,
  CONSTRAINT pk_category PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE category
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Table: manufacturer

-- DROP TABLE manufacturer;

CREATE TABLE manufacturer
(
  id serial NOT NULL,
  name character varying(30) NOT NULL,
  CONSTRAINT pk_manufacturer PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE manufacturer
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Table: measure_unit

-- DROP TABLE measure_unit;

CREATE TABLE measure_unit
(
  id serial NOT NULL,
  name character varying(30) NOT NULL,
  CONSTRAINT pk_measure_unit PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE measure_unit
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Table: presentation

-- DROP TABLE presentation;

CREATE TABLE presentation
(
  id serial NOT NULL,
  name character varying(30) NOT NULL,
  CONSTRAINT pk_presentation PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE presentation
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Table: drug

-- DROP TABLE drug;

CREATE TABLE drug
(
  id serial NOT NULL,
  name character varying(30) NOT NULL,
  CONSTRAINT pk_drug PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE drug
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Table: product

-- DROP TABLE product;

CREATE TABLE product
(
  id serial NOT NULL,
  name character varying(100) NOT NULL,
  description character varying,
  minimum_stock numeric(17,4),
  unit_price numeric(17,4),
  last_update_on timestamp without time zone,
  deleted_on timestamp without time zone,
  last_update_user character varying,
  cost numeric(17,4),
  utility numeric(17,4),
  category integer,
  presentation integer,
  measure_unit integer NOT NULL,
  manufacturer integer,
  active boolean,
  CONSTRAINT pk_product PRIMARY KEY (id),
  CONSTRAINT fk_category_category FOREIGN KEY (category)
      REFERENCES category (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_manufacturer_manufacturer FOREIGN KEY (manufacturer)
      REFERENCES manufacturer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_measure_unit_measure_unit FOREIGN KEY (measure_unit)
      REFERENCES measure_unit (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_presentation_presentation FOREIGN KEY (presentation)
      REFERENCES presentation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product
  OWNER TO vet;

-- Index: fki_category_category

-- DROP INDEX fki_category_category;

CREATE INDEX fki_category_category
  ON product
  USING btree
  (category);

-- Index: fki_manufacturer_manufacturer

-- DROP INDEX fki_manufacturer_manufacturer;

CREATE INDEX fki_manufacturer_manufacturer
  ON product
  USING btree
  (manufacturer);

-- Index: fki_measure_unit_measure_unit

-- DROP INDEX fki_measure_unit_measure_unit;

CREATE INDEX fki_measure_unit_measure_unit
  ON product
  USING btree
  (measure_unit);

-- Index: fki_presentation_presentation

-- DROP INDEX fki_presentation_presentation;

CREATE INDEX fki_presentation_presentation
  ON product
  USING btree
  (presentation);


------------------------------------------------
------------------------------------------------
-- Table: batch

-- DROP TABLE batch;

CREATE TABLE batch
(
  id serial NOT NULL,
  stock numeric(17,4),
  last_update_on timestamp without time zone,
  deleted_on timestamp without time zone,
  last_update_user character varying,
  product integer NOT NULL,
  iso_due_date integer,
  CONSTRAINT pk_batch PRIMARY KEY (id),
  CONSTRAINT fk_product_product FOREIGN KEY (product)
      REFERENCES product (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_iso_due_date_product UNIQUE (iso_due_date, product)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE batch
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Table: product_drugs

-- DROP TABLE product_drugs;

CREATE TABLE product_drugs
(
  product integer NOT NULL,
  drug integer NOT NULL,
  CONSTRAINT pk_product_drugs PRIMARY KEY (product, drug),
  CONSTRAINT fk_drug_drug FOREIGN KEY (drug)
      REFERENCES drug (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_product_product FOREIGN KEY (product)
      REFERENCES product (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product_drugs
  OWNER TO vet;