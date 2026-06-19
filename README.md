SISTEMA DE GESTION ACADEMICA

INTEGRANTES -Nicolas Chauca -Gabriel Yañez

DESCRIPCION Sistema desarrollado con arquitectura de microservicios usando spring boot para la getion academica de una institucion, implementa testing, docker, Swagger, 
Api Gateway, YAML.

EL SISTEMA PERMITE ADMINISTRAR -Estudiantes -Profesores -Facultades -Cursos -Inscripciones -Evaluaciones -Notas -Asistencia -Horarios -Historial academico


MICROSERVICIOS Y FUNCION

1. ms-auth / seguridad y autenticacion
2. ms-facultad / gestion de facultades
3. ms-estudiantes / gestion de estudiantes
4. ms-profesores / gestion de profesores
5. ms-cursos / gestion de cursos
6. ms-inscripciones / inscripcion de estudiantes
7. ms-evaluaciones / gestion de evaluaciones
8. ms-notas / gestion de notas
9. ms-asistencia / control de asistencia
10. ms-horario / gestion de horario
11. ms-historial-academico / resumen academico
12. ms-gateway / permite la seguridad y el enrutamiento a los microservicios
13. ms-eureka / registra los servicios y distribuye el trafico


JACOCO

cd "ms-facultad"
.\mvnw.cmd clean test
start target\site\jacoco\index.html

cd "ms-estudiantes"
.\mvnw.cmd clean test
start target\site\jacoco\index.html

cd "ms-profesores"
.\mvnw.cmd clean test
start target\site\jacoco\index.html

cd "ms-cursos"
.\mvnw.cmd clean test
start target\site\jacoco\index.html

cd "ms-inscripciones"
.\mvnw.cmd clean test
start target\site\jacoco\index.html

cd "ms-evaluaciones"
.\mvnw.cmd clean test
start target\site\jacoco\index.html

cd "ms-notas"
.\mvnw.cmd clean test
start target\site\jacoco\index.html

cd "ms-asistencia"
.\mvnw.cmd clean test
start target\site\jacoco\index.html

cd "ms-horario"
.\mvnw.cmd clean test
start target\site\jacoco\index.html

cd "ms-historial-academico"
.\mvnw.cmd clean test
start target\site\jacoco\index.html




En ms-eureka
Compilar:
.\mvnw.cmd clean compile
Ejecutar:
.\mvnw.cmd spring-boot:run

En ms-gateway
Después de agregar Eureka Client:
Compilar:
.\mvnw.cmd clean compile
Ejecutar:
.\mvnw.cmd spring-boot:run

ms

http://localhost:8092/auth/register 
http://localhost:8082/api/v1/facultades 
http://localhost:8083/api/v1/estudiantes 
http://localhost:8084/api/v1/profesores 
http://localhost:8085/api/v1/cursos 
http://localhost:8086/api/v1/inscripciones 
http://localhost:8087/api/v1/evaluaciones 
http://localhost:8088/api/v1/notas 
http://localhost:8089/api/v1/asistencias 
http://localhost:8090/api/v1/horarios 
http://localhost:8091/api/v1/historial-academico



cd preuba_fullstack-main

cd "ms-auth\ms-auth"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-auth .
cd ..\..
cd "ms-facultad"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-facultad .
cd ..

cd "ms-estudiantes"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-estudiantes .
cd ..

cd "ms-profesores"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-profesores .
cd ..

cd "ms-cursos"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-cursos .
cd ..

cd "ms-inscripciones"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-inscripciones .
cd ..

cd "ms-evaluaciones"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-evaluaciones .
cd ..

cd "ms-notas"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-notas .
cd ..

cd "ms-asistencia"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-asistencias .
cd ..

cd "ms-horario"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-horarios .
cd ..

cd "ms-historial-academico"
.\mvnw.cmd clean package -DskipTests
docker build --no-cache -t ms-historial-academico .
cd ..
docker images




docker exec -it mysql mysql -uroot -proot


CREATE DATABASE ms_user;
CREATE DATABASE ms_facultad;
CREATE DATABASE ms_estudiantes;
CREATE DATABASE ms_profesor;
CREATE DATABASE ms_cursos;
CREATE DATABASE ms_inscripciones;
CREATE DATABASE ms_evaluaciones;
CREATE DATABASE ms_notas;
CREATE DATABASE ms_asistencia;
CREATE DATABASE ms_horario;
CREATE DATABASE ms_historial_academico;

exit




docker compose up -d
docker ps



Si quieres apagar todo Compose junto, más fácil por terminal:
docker compose stop
Para volver a prender:
docker compose start
