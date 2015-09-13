-- Table: product

-- DROP TABLE product;

CREATE TABLE product
(
  id serial NOT NULL,
  name character varying(100) NOT NULL,
  description character varying,
  minimum_stock numeric,
  unitary_price numeric,
  last_update_on timestamp without time zone,
  deleted_on timestamp without time zone,
  last_update_user character varying,
  CONSTRAINT id_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product
  OWNER TO vet;
