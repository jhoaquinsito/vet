--liquibase formatted sql
--changeset tomas:product_active_not_null splitStatements:false

ALTER TABLE product
   ALTER COLUMN active SET NOT NULL;