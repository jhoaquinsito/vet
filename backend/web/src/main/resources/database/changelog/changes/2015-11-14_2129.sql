--liquibase formatted sql
--changeset tomas:person_implementation splitStatements:false


-- IMPLEMENTACION DE TABLAS PERSONA Y ASOCIACIONES

-- Tablas que contiene este changeset (en orden):
-- IVA_category
-- state
-- city
-- person
-- real_person
-- legal_person

------------------------------------------------
------------------------------------------------
-- Table: IVA_category

-- DROP TABLE IVA_category;

CREATE TABLE IVA_category
(
  id serial NOT NULL,
  description character varying(100) NOT NULL,
  CONSTRAINT pk_IVA_category PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE IVA_category
  OWNER TO vet;


------------------------------------------------
------------------------------------------------
-- Table: state

-- DROP TABLE state;

CREATE TABLE state
(
  id serial NOT NULL,
  name character varying(100) NOT NULL,
  last_update_on timestamp without time zone,
  deleted_on timestamp without time zone,
  last_update_user integer,
  CONSTRAINT pk_state PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE state
  OWNER TO vet;


------------------------------------------------
------------------------------------------------
-- Table: city

-- DROP TABLE city;

CREATE TABLE city
(
  id serial NOT NULL,
  name character varying(100) NOT NULL,
  state integer NOT NULL,
  last_update_on timestamp without time zone,
  deleted_on timestamp without time zone,
  last_update_user integer,
  CONSTRAINT pk_city PRIMARY KEY (id),
  CONSTRAINT fk_state_state FOREIGN KEY (state)
      REFERENCES state (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE city
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Table: person

-- DROP TABLE person;

CREATE TABLE person
(
  id serial NOT NULL,
  name character varying(30) NOT NULL,
  address character varying(100),
  phone integer,
  mobile_phone integer,
  email character varying(30),
  renspa numeric(17),
  positive_balance numeric(17,4),
  city integer,
  IVA_category integer,
  last_update_on timestamp without time zone,
  deleted_on timestamp without time zone,
  last_update_user integer,
  CONSTRAINT pk_person PRIMARY KEY (id),
  CONSTRAINT fk_city_city FOREIGN KEY (city)
      REFERENCES city (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_IVA_category_IVA_category FOREIGN KEY (IVA_category)
      REFERENCES IVA_category (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE person
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Table: real_person

-- DROP TABLE real_person;

CREATE TABLE real_person
(
  person_id integer NOT NULL,
  document_id integer NOT NULL,
  last_name character varying(30) NOT NULL,
  last_update_on timestamp without time zone,
  deleted_on timestamp without time zone,
  last_update_user integer,
  CONSTRAINT uk_real_person_document_id UNIQUE (document_id),
  CONSTRAINT pk_real_person PRIMARY KEY (person_id),
  CONSTRAINT fk_person_person_id FOREIGN KEY (person_id)
    REFERENCES person (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE real_person
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Table: legal_person

-- DROP TABLE legal_person;

CREATE TABLE legal_person
(
  person_id integer NOT NULL,
  cuit numeric(11,0) NOT NULL,
  last_update_on timestamp without time zone,
  deleted_on timestamp without time zone,
  last_update_user integer,
  CONSTRAINT uk_legal_person_cuit UNIQUE (cuit),
  CONSTRAINT pk_legal_person PRIMARY KEY (person_id),
  CONSTRAINT fk_person_person_id FOREIGN KEY (person_id)
      REFERENCES person (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE legal_person
  OWNER TO vet;

