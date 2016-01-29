--liquibase formatted sql
--changeset ggorosito:sale_saleline_drop_and_recreate splitStatements:false


-- Modificaciones que contiene este changeset (en orden):
--- Borrado y creacion nuevamente de la tabla sale.
--- Borrado y creacion nuevamente de la tabla saleline.
--- Con esto se evita el problema del owner.

------------------------------------------------
------------------------------------------------


DROP TABLE saleline;

DROP TABLE sale;


-- Table: sale



CREATE TABLE sale
(
  id serial NOT NULL,
  date timestamp without time zone DEFAULT now(),
  invoiced boolean NOT NULL,
  paied_out boolean NOT NULL,
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
  OWNER TO vet;
  
  -- Table: saleline
  
CREATE TABLE saleline
(
  id serial NOT NULL ,
  sale_id integer NOT NULL,
  product_id integer NOT NULL,
  quantity double precision DEFAULT 0,
  unit_price numeric(17,4) DEFAULT 0,
  discount numeric(4,2) DEFAULT 0.0,
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
  OWNER TO vet;
  
