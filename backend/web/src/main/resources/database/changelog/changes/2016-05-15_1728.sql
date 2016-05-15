--liquibase formatted sql
--changeset tomas:addauditfields_remove_deleted_on splitStatements:false


CREATE OR REPLACE FUNCTION addauditfields()
  RETURNS trigger AS
$BODY$
BEGIN
NEW.last_update_on  := LOCALTIMESTAMP;
NEW.last_update_user := 'hardcoded user';
RETURN NEW;
END $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION addauditfields()
  OWNER TO vet;