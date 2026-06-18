create table horarios (
    id integer not null AUTO_INCREMENT,
    dia varchar(20) not null,
    hora_inicio varchar(10) not null,
    hora_fin varchar(10) not null,
    sala varchar(50) not null,
    id_curso integer,
    PRIMARY KEY (id)
);

INSERT INTO horarios (dia, hora_inicio, hora_fin, sala, id_curso)
VALUES ('Lunes', '08:30', '10:00', 'Lab Backend', 1);

INSERT INTO horarios (dia, hora_inicio, hora_fin, sala, id_curso)
VALUES ('Miércoles', '10:15', '11:45', 'Sala Física', 2);