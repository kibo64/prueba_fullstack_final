create table historial_academico (
    id integer not null AUTO_INCREMENT,
    promedio_final double not null,
    asistencia_final double not null,
    estado varchar(50),
    id_estudiante integer,
    id_curso integer,
    PRIMARY KEY (id)
);

INSERT INTO historial_academico 
(promedio_final, asistencia_final, estado, id_estudiante, id_curso)
VALUES 
(5.8, 85.0, 'Aprobado', 1, 1);

INSERT INTO historial_academico 
(promedio_final, asistencia_final, estado, id_estudiante, id_curso)
VALUES 
(3.5, 60.0, 'Reprobado', 2, 2);