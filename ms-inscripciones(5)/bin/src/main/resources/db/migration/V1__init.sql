CREATE TABLE inscripciones (
    id INTEGER NOT NULL AUTO_INCREMENT,
    fecha DATE,
    id_estudiante INTEGER,
    id_curso INTEGER,
    PRIMARY KEY (id)
);

INSERT INTO inscripciones (fecha, id_estudiante, id_curso)
VALUES ('2026-05-08', 1, 1);

INSERT INTO inscripciones (fecha, id_estudiante, id_curso)
VALUES ('2026-05-08', 2, 2);