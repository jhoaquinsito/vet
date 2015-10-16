--liquibase formatted sql
--changeset tomas:drugs_to_drug splitStatements:false


-- elimino tabla que relacionaba muchos a muchos
DROP TABLE product_drugs;

-- agrego campo droga al producto
ALTER TABLE product
  ADD COLUMN drug integer;
ALTER TABLE product
  ADD CONSTRAINT fk_drug_drug FOREIGN KEY (drug) REFERENCES drug (id) ON UPDATE NO ACTION ON DELETE NO ACTION;