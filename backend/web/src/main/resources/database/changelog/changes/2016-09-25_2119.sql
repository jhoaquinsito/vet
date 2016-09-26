--liquibase formatted sql
--changeset tomas:update_settlement_discounted_new_check_constraint splitStatements:false

-- agrego constraint para que discounted no pueda ser mayor que amount
ALTER TABLE settlement ADD CONSTRAINT check_discounted CHECK (discounted <= amount);