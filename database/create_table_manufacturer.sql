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
