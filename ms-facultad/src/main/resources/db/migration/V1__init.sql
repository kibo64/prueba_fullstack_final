create table facultad (
    id integer not null auto_increment,
    nombre varchar(100) not null,
    ubicacion varchar(100),
    primary key (id)
);
INSERT INTO facultad (nombre, ubicacion)VALUES ('Ingenieria', 'Campus Norte');
INSERT INTO facultad (nombre, ubicacion)VALUES ('Medicina', 'Campus Sur');
