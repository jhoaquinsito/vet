--liquibase formatted sql
--changeset lautaro:insert_iva_categories splitStatements:false

-- inserto las categorías de IVA
INSERT INTO iva_category (description, percentage) VALUES
	('IVA Responsable Inscripto', 21),
	('IVA Responsable no Inscripto', 21),
	('IVA no Responsable', 21),
	('IVA Sujeto Exento', 0),
	('Consumidor Final', 21),
	('Responsable Monotributo', 21),
	('Sujeto no Categorizado', 21),
	('Proveedor del Exterior', 21),
	('Cliente del Exterior'	, 21),
	('IVA Liberado – Ley Nº 19.640', 21),
	('IVA Responsable Inscripto – Agente de Percepción', 21),
	('Pequeño Contribuyente Eventual', 21),
	('Monotributista Social', 21),
	('Pequeño Contribuyente Eventual Social', 21);

