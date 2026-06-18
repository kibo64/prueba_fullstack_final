create table profesor (
    id integer not null auto_increment,
    rutP varchar(12) not null unique,
    nombreP varchar(100) not null,
    emailP varchar(100) not null,
    especialidad varchar(100),
    id_facultad integer,
    primary key (id)
);
INSERT INTO profesor (rutP, nombreP, emailP, especialidad, id_facultad)
VALUES ('11111111-1', 'Pedro', 'pedro@test.com', 'Matematicas', 1);
INSERT INTO profesor (rutP, nombreP, emailP, especialidad, id_facultad)
VALUES ('22222222-2', 'Juan', 'juan@test.com', 'Física', 2);