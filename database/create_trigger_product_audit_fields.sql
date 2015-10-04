-- Trigger: auditfieldstriggerforproduct on product

-- DROP TRIGGER auditfieldstriggerforproduct ON product;

CREATE TRIGGER auditfieldstriggerforproduct
  BEFORE INSERT OR UPDATE
  ON product
  FOR EACH ROW
  EXECUTE PROCEDURE addauditfields();
