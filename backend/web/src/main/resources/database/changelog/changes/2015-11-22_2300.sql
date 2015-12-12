--liquibase formatted sql
--changeset ggorosito:person_implementation splitStatements:false


-- IMPLEMENTACION DE TABLAS DE RELACION "Supplier"

-- Tablas que contiene este changeset (en orden):
-- Create table de supplier

------------------------------------------------
------------------------------------------------
-- Table: supplier

-- DROP TABLE supplier;

CREATE TABLE supplier
(
  product_id integer NOT NULL,
  legal_person_id integer NOT NULL,
  
  CONSTRAINT pk_supplier PRIMARY KEY (product_id,legal_person_id),
  
  CONSTRAINT fk_supplier_product_id FOREIGN KEY (product_id)
    REFERENCES product (id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
	
  CONSTRAINT fk_supplier_legal_person_id FOREIGN KEY (legal_person_id)
    REFERENCES legal_person (person_id) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE supplier
  OWNER TO vet;

