--liquibase formatted sql
--changeset lautaro:generic_client splitStatements:false


-- Agrego un cliente generico al cual se deberan asociar todos las ventas y los pagos 
-- para los que no se indique un cliente específico que exista en la bd

-- Borro la persona físicas a la persona con id 1
DELETE FROM natural_person
WHERE person_id = 1;

-- Borro la persona legal asociada a la persona con id 1
DELETE FROM supplier
WHERE legal_person_id = 1;

DELETE FROM legal_person
WHERE person_id = 1;

-- Borro los pagos asociadas a la persona con id 1
DELETE FROM settlement
WHERE person = 1;

-- Borro las ventas asociadas a la persona con id 1
DELETE FROM saleline
WHERE sale_id IN (SELECT id FROM sale WHERE person = 1);

DELETE FROM sale
WHERE person = 1;

-- Borro la persona con id 1
DELETE FROM person
WHERE id = 1;

-- Inserto el cliente genérico con id 1
INSERT INTO person(id, "name", address, phone, mobile_phone, email, active)
    VALUES (1, 'Sin Identificar', 'Sin Identificar', 'Sin Identificar', 'Sin Identificar', 'Sin Identificar', TRUE);
