-- Table: category

-- DROP TABLE category;

CREATE TABLE category
(
  id serial NOT NULL,
  name character varying(30) NOT NULL,
  CONSTRAINT pk_category PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE category
  OWNER TO vet;