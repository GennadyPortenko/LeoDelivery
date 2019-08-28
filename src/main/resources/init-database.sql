-- DROP SCHEMA IF EXISTS public CASCADE;
-- CREATE SCHEMA public;

DROP TABLE IF EXISTS client CASCADE;
CREATE TABLE client (
  client_id BIGSERIAL PRIMARY KEY
, phone VARCHAR(50) NOT NULL
, email VARCHAR(150)
, active INTEGER NOT NULL
);

DROP TABLE IF EXISTS contractor CASCADE;
CREATE TABLE contractor (
  contractor_id BIGSERIAL PRIMARY KEY
, name VARCHAR(50) NOT NULL
, email VARCHAR(150) NOT NULL
, image VARCHAR(150)
, password VARCHAR(200) NOT NULL
, active INTEGER NOT NULL
);

DROP TABLE IF EXISTS role CASCADE;
CREATE TABLE role (
  role_id SERIAL PRIMARY KEY
, role VARCHAR(20) NOT NULL
);

DROP TABLE IF EXISTS city CASCADE;
CREATE TABLE city (
  city_id SERIAL PRIMARY KEY
, name VARCHAR(50) NOT NULL
, french_name VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS district CASCADE;
CREATE TABLE district (
  district_id SERIAL PRIMARY KEY
, name VARCHAR(50) NOT NULL
, french_name VARCHAR(50) NOT NULL
, city_fk INTEGER REFERENCES city(city_id) -- ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS client_role CASCADE;
CREATE TABLE client_role (
  client_id INTEGER REFERENCES client(client_id)
, role_id INTEGER REFERENCES role(role_id)
, CONSTRAINT client_role_pkey PRIMARY KEY (client_id, role_id)
);

DROP TABLE IF EXISTS contractor_role CASCADE;
CREATE TABLE contractor_role (
  contractor_id INTEGER REFERENCES contractor(contractor_id)
, role_id INTEGER REFERENCES role(role_id)
, CONSTRAINT contractor_role_pkey PRIMARY KEY (contractor_id, role_id)
);

DROP TABLE IF EXISTS section CASCADE;
CREATE TABLE section (
  section_id BIGSERIAL PRIMARY KEY
, name VARCHAR(50) NOT NULL
, description TEXT
, contractor_fk INTEGER REFERENCES contractor(contractor_id) -- ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS product CASCADE;
CREATE TABLE product (
  product_id BIGSERIAL PRIMARY KEY
, name VARCHAR(50) NOT NULL
, description TEXT NOT NULL
, section_fk INTEGER REFERENCES section(section_id) -- ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS category CASCADE;
CREATE TABLE category (
  category_id SERIAL PRIMARY KEY
, name VARCHAR(50) NOT NULL
, french_name VARCHAR(50) NOT NULL
);


-- roles initialization

INSERT INTO role (role_id, role)
VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER'), (3, 'ROLE_CONTRACTOR')
ON CONFLICT (role_id) DO UPDATE
  SET role = excluded.role;


-- categories initialization

INSERT INTO category (category_id, name, french_name)
VALUES (1, 'Pizza', 'Pizza'), (2, 'Sushi', 'Sushi')
ON CONFLICT (category_id) DO UPDATE
  SET name = excluded.name, french_name = excluded.french_name;


-- cities / districts initialization

INSERT INTO city (city_id, name, french_name)
VALUES (1, 'Douala', 'Douala'), (2, 'Yaounde', 'Yaoundé')
ON CONFLICT (city_id) DO UPDATE
  SET name = excluded.name, french_name = excluded.french_name;

INSERT INTO district (district_id, name, french_name, city_fk)
VALUES (1, 'Akwa', 'Akwa', 1), (2, 'Bassa', 'Bassa', 1),
       (3, 'Etoudi', 'Etoudi', 2), (4, 'Bastos', 'Bastos', 2)
ON CONFLICT (district_id) DO UPDATE
  SET name = excluded.name, french_name = excluded.french_name;
