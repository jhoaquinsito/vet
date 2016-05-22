--liquibase formatted sql
--changeset lautaro:sales_by_batch splitStatements:false

-- limpio la tabla saleline
DELETE FROM saleline;

-- elimino la UK y la FK para poder eliminar la columna product_id
ALTER TABLE saleline DROP CONSTRAINT saleline_key;
ALTER TABLE saleline DROP CONSTRAINT fk_saleline_product_id; 

-- elimino la columna producto de saleline
ALTER TABLE saleline DROP COLUMN product_id;

-- agrego la columna batch a saleline
ALTER TABLE saleline ADD COLUMN batch INT;

-- creo las restricciones de integridad para saleline
ALTER TABLE saleline ADD CONSTRAINT fk_saleline_batch FOREIGN KEY (batch) REFERENCES batch (id);
ALTER TABLE saleline ADD CONSTRAINT uk_sale_batch_saleline UNIQUE (sale_id, batch);

