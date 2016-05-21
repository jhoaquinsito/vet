--liquibase formatted sql
--changeset tomas:measure_unit_abbreviation_set_L_litros splitStatements:false


UPDATE measure_unit SET abbreviation = 'L.' where id=2