-- Table: product_drugs

-- DROP TABLE product_drugs;

CREATE TABLE product_drugs
(
  product integer NOT NULL,
  drug integer NOT NULL,
  CONSTRAINT pk_product_drugs PRIMARY KEY (product, drug),
  CONSTRAINT fk_drug_drug FOREIGN KEY (drug)
      REFERENCES drug (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_product_product FOREIGN KEY (product)
      REFERENCES product (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product_drugs
  OWNER TO vet;
