--liquibase formatted sql
--changeset lautaro:update_product_table splitStatements:false

-- elimino la restricción UNIQUE del nombre del producto
ALTER TABLE product DROP CONSTRAINT IF EXISTS uk_product_name;

-- agrego el atributo IVA
ALTER TABLE product ADD COLUMN iva NUMERIC(4,2) DEFAULT 0;

-- agrego el valor por defecto a la columna active
ALTER TABLE product ALTER COLUMN active SET DEFAULT TRUE;
