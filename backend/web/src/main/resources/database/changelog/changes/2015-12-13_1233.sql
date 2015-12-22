--liquibase formatted sql
--changeset ggorosito:legalPerson_client splitStatements:false


-- elimino la columna lastname de persona ya que es un atributo solo de persona física
ALTER TABLE person DROP COLUMN lastname;


-- agrego el atributo booleano client a legal_person para identificar si es cliente
-- el atributo permite 3 valores: true (es solo cliente), false (es solo preoveedor), NULL (es ambos)

ALTER TABLE legal_person ADD COLUMN client boolean;

