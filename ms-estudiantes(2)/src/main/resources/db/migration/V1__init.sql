create table estudiante (
    id integer not null auto_increment,
    rut varchar(12) not null unique,
    nombre varchar(100) not null,
    email varchar(100) not null,
    carrera varchar(100),
    id_facultad integer,
    primary key (id)
);

INSERT INTO estudiante (rut, nombre, email, carrera, id_facultad)
VALUES ('12345678-9', 'Nico', 'nico@test.com', 'Ingenieria', 1);

INSERT INTO estudiante (rut, nombre, email, carrera, id_facultad)
VALUES ('98765432-1', 'Juan', 'juan@test.com', 'Medicina', 2);