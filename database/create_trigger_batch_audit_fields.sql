-- Trigger: auditfieldstriggerforbatch on batch

-- DROP TRIGGER auditfieldstriggerforbatch ON batch;

CREATE TRIGGER auditfieldstriggerforbatch
  BEFORE INSERT OR UPDATE
  ON batch
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();
