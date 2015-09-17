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