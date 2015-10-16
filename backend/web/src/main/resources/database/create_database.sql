-- Creación del rol que usaremos en la base de datos

-- Role: vet

-- DROP ROLE vet;

CREATE ROLE vet LOGIN
  ENCRYPTED PASSWORD 'md51d6fd74ace029809a2092af542bcbea7'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
COMMENT ON ROLE vet IS 'vet
vet';


-- Creación de la base de datos

-- Database: vet

-- DROP DATABASE vet;

CREATE DATABASE vet
  WITH OWNER = vet
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'es_AR.UTF-8'
       LC_CTYPE = 'es_AR.UTF-8'
       CONNECTION LIMIT = -1;
