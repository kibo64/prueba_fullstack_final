create table asistencias (
    id integer not null AUTO_INCREMENT,
    fecha_clase DATE not null,
    asistencia boolean not null,
    id_curso integer,
    id_estudiante integer,
    PRIMARY KEY (id)
);

INSERT INTO asistencias (fecha_clase, asistencia, id_curso, id_estudiante)
VALUES ('2026-05-08', true, 1, 1);

INSERT INTO asistencias (fecha_clase, asistencia, id_curso, id_estudiante)
VALUES ('2026-05-09', false, 2, 2);