--liquibase formatted sql
--changeset ggorosito:delete_active_not_nul splitStatements:false

-- elimino la restricción not null del atributo active de person
-- ya que es redudante con el el valor por defecto
ALTER TABLE person ALTER COLUMN active DROP NOT NULL;
