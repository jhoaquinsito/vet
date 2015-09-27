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

