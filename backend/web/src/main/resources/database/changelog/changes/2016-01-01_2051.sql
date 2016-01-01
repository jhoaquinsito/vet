--liquibase formatted sql
--changeset ggorosito:sale_implementation splitStatements:false


-- IMPLEMENTACION DE TABLAS "Sale" & "SaleLine"

-- Tablas que contiene este changeset (en orden):
--- Create table de sale
--- Create table de saleline

------------------------------------------------
------------------------------------------------

-- Table: sale

-- DROP TABLE sale;

CREATE TABLE sale
(
  id serial NOT NULL,
  date timestamp without time zone,
  invoiced boolean NOT NULL DEFAULT false,
  paied_out boolean NOT NULL DEFAULT false,
  person integer,
  CONSTRAINT pk_sale PRIMARY KEY (id),
  CONSTRAINT fk_person_person FOREIGN KEY (person)
      REFERENCES person (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sale
  OWNER TO postgres;
GRANT ALL ON TABLE sale TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE sale TO public;
  
-- Table: saleline

-- DROP TABLE saleline;

CREATE TABLE saleline
(
  id integer NOT NULL DEFAULT nextval('saleline_saleline_id_seq'::regclass),
  sale_id integer NOT NULL,
  product_id integer NOT NULL,
  quantity double precision NOT NULL DEFAULT 0,
  unit_price numeric(17,4) NOT NULL DEFAULT 0,
  CONSTRAINT saleline_pkey PRIMARY KEY (id),
  CONSTRAINT fk_saleline_product_id FOREIGN KEY (product_id)
      REFERENCES product (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_saleline_sale_id FOREIGN KEY (sale_id)
      REFERENCES sale (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT saleline_key UNIQUE (id, sale_id, product_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE saleline
  OWNER TO postgres;
GRANT ALL ON TABLE saleline TO postgres;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE saleline TO public;
  