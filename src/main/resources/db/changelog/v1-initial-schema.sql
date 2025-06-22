CREATE TABLE role (
                      role_id BIGSERIAL PRIMARY KEY,
                      role_name VARCHAR(255) NOT NULL UNIQUE,
                      access_level INTEGER NOT NULL
);

CREATE TABLE staff (
                       staff_id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255) NOT NULL,
                       middle_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       position VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role_id BIGINT REFERENCES role(role_id),
                       contact VARCHAR(255) NOT NULL
);

CREATE TABLE equipment (
                           equipment_id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           purchase_date DATE NOT NULL,
                           type VARCHAR(50) NOT NULL,
                           status VARCHAR(50) NOT NULL,
                           staff_id BIGINT REFERENCES staff(staff_id)
);

CREATE TABLE incidents (
                           incident_id BIGSERIAL PRIMARY KEY,
                           equipment_id BIGINT REFERENCES equipment(equipment_id),
                           staff_id BIGINT REFERENCES staff(staff_id),
                           date DATE NOT NULL,
                           status VARCHAR(50) NOT NULL
);

CREATE TABLE maintenance (
                             maintenance_id BIGSERIAL PRIMARY KEY,
                             equipment_id BIGINT REFERENCES equipment(equipment_id),
                             staff_id BIGINT REFERENCES staff(staff_id),
                             date DATE NOT NULL,
                             description TEXT,
                             type VARCHAR(50) NOT NULL
);