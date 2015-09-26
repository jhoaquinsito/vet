-- Table: batch

-- DROP TABLE batch;

CREATE TABLE batch
(
  id serial NOT NULL,
  due_date date,
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
  CONSTRAINT uk_iso_due_date UNIQUE (iso_due_date)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE batch
  OWNER TO vet;
