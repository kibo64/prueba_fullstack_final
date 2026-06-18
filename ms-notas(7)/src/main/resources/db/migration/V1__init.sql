create table notas (
    id integer not null AUTO_INCREMENT,
    nota double not null,
    id_estudiante integer,
    id_evaluacion integer,
    PRIMARY KEY (id)
);

INSERT INTO notas (nota, id_estudiante, id_evaluacion)
VALUES (6.0, 1, 1);

INSERT INTO notas (nota, id_estudiante, id_evaluacion)
VALUES (5.5, 2, 2);