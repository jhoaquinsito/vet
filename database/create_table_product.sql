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
  CONSTRAINT pk_product PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product
  OWNER TO vet;