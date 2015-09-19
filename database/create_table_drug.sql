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
