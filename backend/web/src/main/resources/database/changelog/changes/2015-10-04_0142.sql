--liquibase formatted sql
--changeset tomas:triggers splitStatements:false


------------------------------------------------
------------------------------------------------
-- Function: addauditfields()

-- DROP FUNCTION addauditfields();

CREATE OR REPLACE FUNCTION addauditfields()
  RETURNS trigger AS
$BODY$
BEGIN
NEW.last_update_on  := LOCALTIMESTAMP;
NEW.deleted_on  := LOCALTIMESTAMP;
NEW.last_update_user := 'hardcode user';
RETURN NEW;
END $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION addauditfields()
  OWNER TO vet;

------------------------------------------------
------------------------------------------------
-- Trigger: auditfieldstriggerforproduct on product

-- DROP TRIGGER auditfieldstriggerforproduct ON product;

CREATE TRIGGER auditfieldstriggerforproduct
  BEFORE INSERT OR UPDATE
  ON product
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();

------------------------------------------------
------------------------------------------------
-- Trigger: auditfieldstriggerforbatch on batch

-- DROP TRIGGER auditfieldstriggerforbatch ON batch;

CREATE TRIGGER auditfieldstriggerforbatch
  BEFORE INSERT OR UPDATE
  ON batch
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();