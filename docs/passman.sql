CREATE DATABASE passman;

USE passman; 

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    password_id INT,
    creationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password FOREIGN KEY (password_id) REFERENCES passwords(id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- CREATE TABLE passwords (
--     id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
--     title VARCHAR(50) NOT NULL,
--     username VARCHAR(50) NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     creationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     modiifedDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT domain_id FOREIGN KEY (id) REFERENCES domains(id),
--     CONSTRAINT tag_id FOREIGN KEY (id) REFERENCES tags(id)
-- );

CREATE TABLE passwords (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    title VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    domain_id INT,
    tag_id INT,
    creationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modiifedDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_domain FOREIGN KEY (domain_id) REFERENCES domains(id),
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tags(id)
);

CREATE TABLE domains (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    domain VARCHAR(255) NOT NULL
);

CREATE TABLE tags (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    creationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modiifedDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Dummy data
INSERT INTO tags (name)
VALUE ("web")

INSERT INTO domains(domain)
VALUE("google.com")