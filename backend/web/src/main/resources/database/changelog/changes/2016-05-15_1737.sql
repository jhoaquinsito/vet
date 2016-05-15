--liquibase formatted sql
--changeset tomas:add_missing_triggers splitStatements:false

CREATE TRIGGER auditfieldstriggerforcity
  BEFORE INSERT OR UPDATE
  ON city
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();

CREATE TRIGGER auditfieldstriggerforlegalperson
  BEFORE INSERT OR UPDATE
  ON legal_person
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();

CREATE TRIGGER auditfieldstriggerfornaturalperson
  BEFORE INSERT OR UPDATE
  ON natural_person
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();

CREATE TRIGGER auditfieldstriggerforperson
  BEFORE INSERT OR UPDATE
  ON person
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();

CREATE TRIGGER auditfieldstriggerforsettlement
  BEFORE INSERT OR UPDATE
  ON settlement
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();

CREATE TRIGGER auditfieldstriggerforstate
  BEFORE INSERT OR UPDATE
  ON state
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();