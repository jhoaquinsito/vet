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