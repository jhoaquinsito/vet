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
