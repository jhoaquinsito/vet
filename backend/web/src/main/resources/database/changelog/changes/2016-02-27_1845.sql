--liquibase formatted sql
--changeset lautaro:final_consumer splitStatements:false

-- modifico el nombre de la persona con id=1 de "Sin identificar" a "Consumidor Final"
-- y la asocio a la categoría de iva Consumidor Final
UPDATE person
SET name='Consumidor Final',
iva_category=5
WHERE id=1;

-- creo la persona física asociada al consumidor final
INSERT INTO natural_person(person_id, national_id, last_name)
    VALUES (1, 0, 'Sin Identificar');