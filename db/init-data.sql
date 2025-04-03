CREATE TABLE roles (
id SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE statuses (
id SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE,
code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE priorities (
id SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE,
code VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (name) VALUES
  ('ROLE_ADMIN'),
  ('ROLE_USER')
ON CONFLICT (name) DO NOTHING;

INSERT INTO statuses (name, code) VALUES
  ('В ожидании', 'PENDING'),
  ('В процессе', 'IN_PROGRESS'),
  ('Завершено', 'COMPLETED')
ON CONFLICT (code) DO NOTHING;

INSERT INTO priorities (name, code) VALUES
  ('Высокий', 'HIGH'),
  ('Средний', 'MEDIUM'),
  ('Низкий', 'LOW')
ON CONFLICT (code) DO NOTHING;