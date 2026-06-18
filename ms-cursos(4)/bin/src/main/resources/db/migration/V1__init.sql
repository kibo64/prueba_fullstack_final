create table cursos (
    id integer not null AUTO_INCREMENT,
    nombreC varchar(100) not null,
    cantidadE integer not null,
    descripcion varchar(255),
    id_Profesor integer,
    id_Facultad integer,
    PRIMARY KEY (id)
);

INSERT INTO cursos (nombreC, cantidadE, descripcion, id_Profesor, id_Facultad)
VALUES ('Programacion_Backend', 30, 'Curso de Spring Boot', 1, 1);

INSERT INTO cursos (nombreC, cantidadE, descripcion, id_Profesor, id_Facultad)
VALUES ('Base de Datos', 25, 'Curso de SQL y MySQL', 2, 2);