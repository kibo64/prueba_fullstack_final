create table evaluaciones (
    id integer not null AUTO_INCREMENT,
    nombree varchar(100) not null,
    fecha DATE,
    ponderacion double,
    id_curso integer,
    PRIMARY KEY (id)
);

INSERT INTO evaluaciones (nombree, fecha, ponderacion, id_curso)
VALUES ('Prueba_1', '2026-05-08', 30, 1);

INSERT INTO evaluaciones (nombree, fecha, ponderacion, id_curso)
VALUES ('Taller 1', '2026-05-15', 20, 2);