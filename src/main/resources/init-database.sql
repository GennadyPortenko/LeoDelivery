-- DROP SCHEMA IF EXISTS public CASCADE;
-- CREATE SCHEMA public;

DROP TABLE IF EXISTS person CASCADE;
CREATE TABLE person (
  person_id SERIAL PRIMARY KEY
, name VARCHAR(50) NOT NULL
, email VARCHAR(150) NOT NULL
, password VARCHAR(200) NOT NULL
, active INTEGER NOT NULL
);

DROP TABLE IF EXISTS contractor CASCADE;
CREATE TABLE contractor (
  contractor_id SERIAL PRIMARY KEY
, name VARCHAR(50) NOT NULL
, email VARCHAR(150) NOT NULL
, password VARCHAR(200) NOT NULL
, active INTEGER NOT NULL
);

DROP TABLE IF EXISTS role CASCADE;
CREATE TABLE role (
  role_id SERIAL PRIMARY KEY
, role VARCHAR(20) NOT NULL
);

DROP TABLE IF EXISTS person_role CASCADE;
CREATE TABLE person_role (
  person_id INTEGER REFERENCES person(person_id)
, role_id INTEGER REFERENCES role(role_id)
, CONSTRAINT person_role_pkey PRIMARY KEY (person_id, role_id)
);

DROP TABLE IF EXISTS contractor_role CASCADE;
CREATE TABLE contractor_role (
  contractor_id INTEGER REFERENCES contractor(contractor_id)
, role_id INTEGER REFERENCES role(role_id)
, CONSTRAINT contractor_role_pkey PRIMARY KEY (contractor_id, role_id)
);

-- roles initialization

INSERT INTO role (role_id, role)
VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER'), (3, 'ROLE_CONTRACTOR')
ON CONFLICT (role_id) DO UPDATE
  SET role = excluded.role;