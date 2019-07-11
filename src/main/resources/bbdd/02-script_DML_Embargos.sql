---*** INSERTS DEL DOCUMENTO CGPJ_analisis.pdf ***---

Insert into TIPO_FICHERO (COD_TIPO_FICHERO,DES_TIPO_FICHERO) values ('3','PETICION CGPJ');
Insert into TIPO_FICHERO (COD_TIPO_FICHERO,DES_TIPO_FICHERO) values ('4','RESPUESTA CGPJ');

Insert into ENTIDADES_COMUNICADORAS (COD_ENTIDAD_PRESENTADORA,DES_ENTIDAD,IDENTIFICADOR_ENTIDAD,IND_CGPJ) values (10, 'Consejo General del Poder Judicial','PJ','S');

Insert into ENTIDADES_ORDENANTES (COD_ENTIDAD_ORDENANTE,COD_ENTIDAD_PRESENTADORA,DES_ENTIDAD,IDENTIFICADOR_ENTIDAD,IND_CGPJ) values (10,10,'Consejo General del Poder Judicial','PJ','S');

-- Inserts en nueva tabla ESTADOS_ENVIO_PETICION --> Acaba siendo la tabla ESTADO_PRIMARIO_PETICION
insert into ESTADO_PRIMARIO_PETICION (COD_ESTADO_PRIMARIO_PETICION, DES_ESTADO_PRIMARIO_PETICION) values ('1000', 'Enviando petición única');
insert into ESTADO_PRIMARIO_PETICION (COD_ESTADO_PRIMARIO_PETICION, DES_ESTADO_PRIMARIO_PETICION) values ('1001', 'Enviando petición particionada');
insert into ESTADO_PRIMARIO_PETICION (COD_ESTADO_PRIMARIO_PETICION, DES_ESTADO_PRIMARIO_PETICION) values ('1002', 'Enviando petición vacía');

-- Inserts en nueva tabla ESTADOS_ENVIO_RESPUESTA --> Acaba siendo la tabla ESTADO_PRIMARIO_RESP
insert into ESTADO_PRIMARIO_RESP (COD_ESTADO_PRIMARIO_RESP, DES_ESTADO_PRIMARIO_RESP) values ('0000','Petición procesada totalmente');
insert into ESTADO_PRIMARIO_RESP (COD_ESTADO_PRIMARIO_RESP, DES_ESTADO_PRIMARIO_RESP) values ('0001','Petición no procesada');
insert into ESTADO_PRIMARIO_RESP (COD_ESTADO_PRIMARIO_RESP, DES_ESTADO_PRIMARIO_RESP) values ('0002','Error general');
insert into ESTADO_PRIMARIO_RESP (COD_ESTADO_PRIMARIO_RESP, DES_ESTADO_PRIMARIO_RESP) values ('0003','Petición caducada');
insert into ESTADO_PRIMARIO_RESP (COD_ESTADO_PRIMARIO_RESP, DES_ESTADO_PRIMARIO_RESP) values ('0004','Petición recibida y pendiente de ser procesada');
insert into ESTADO_PRIMARIO_RESP (COD_ESTADO_PRIMARIO_RESP, DES_ESTADO_PRIMARIO_RESP) values ('0005','Petición ya recibida anteriormente');

-- Inserts en nueva tabla ESTADOS_SECUNDARIOS_ENVIO_RESPUESTA --> Acaba siendo la tabla ESTADO_SECUNDARIO_RESP
insert into ESTADO_SECUNDARIO_RESP (COD_ESTADO_SECUNDARIO_RESP, DES_ESTADO_SECUNDARIO_RESP) values ('1000','Retraso por fiesta nacional');
insert into ESTADO_SECUNDARIO_RESP (COD_ESTADO_SECUNDARIO_RESP, DES_ESTADO_SECUNDARIO_RESP) values ('1001','Retraso por fiesta autonómica');
insert into ESTADO_SECUNDARIO_RESP (COD_ESTADO_SECUNDARIO_RESP, DES_ESTADO_SECUNDARIO_RESP) values ('1002','Retraso por fiesta local');
insert into ESTADO_SECUNDARIO_RESP (COD_ESTADO_SECUNDARIO_RESP, DES_ESTADO_SECUNDARIO_RESP) values ('1003','Otros tipos de retrasos');

-- Inserts nueva tabla ESTADO_INTERNO_PETICION --> Acaba siendo la tabla ESTADO_INT_PETICION
insert into ESTADO_INT_PETICION (COD_ESTADO_INT_PETICION, DES_ESTADO_INT_PETICION) values (1, 'Inicial');
insert into ESTADO_INT_PETICION (COD_ESTADO_INT_PETICION, DES_ESTADO_INT_PETICION) values (2, 'Procesada');
insert into ESTADO_INT_PETICION (COD_ESTADO_INT_PETICION, DES_ESTADO_INT_PETICION) values (3, 'Pendiente de Enviar');
insert into ESTADO_INT_PETICION (COD_ESTADO_INT_PETICION, DES_ESTADO_INT_PETICION) values (4, 'Enviada');
insert into ESTADO_INT_PETICION (COD_ESTADO_INT_PETICION, DES_ESTADO_INT_PETICION) values (5, 'Error');

-- Inserts nueva tabla ESTADO_SOLICITUD_TRABA --> Acaba siendo la tabla ESTADO_INT_TRABA
insert into ESTADO_INT_TRABA (COD_ESTADO_INT_TRABA, DES_ESTADO_INT_TRABA) values (1, 'Inicial');
insert into ESTADO_INT_TRABA (COD_ESTADO_INT_TRABA, DES_ESTADO_INT_TRABA) values (2, 'Pendiente de Contabilizar');
insert into ESTADO_INT_TRABA (COD_ESTADO_INT_TRABA, DES_ESTADO_INT_TRABA) values (3, 'Procesada');
insert into ESTADO_INT_TRABA (COD_ESTADO_INT_TRABA, DES_ESTADO_INT_TRABA) values (4, 'Cancelada');

-- Inserts nueva tabla ESTADO_SOLICITUD_EJECUCION --> Acaba siendo la tabla ESTADO_INT_EJECUCION
insert into ESTADO_INT_EJECUCION (COD_ESTADO_INT_EJECUCION, DES_ESTADO_INT_EJECUCION) values (1, 'Inicial');
insert into ESTADO_INT_EJECUCION (COD_ESTADO_INT_EJECUCION, DES_ESTADO_INT_EJECUCION) values (2, 'Procesada');

-- Inserts nueva tabla ESTADO_SOLICITUD_LEVANTAMIENTO --> Acaba siendo la tabla ESTADO_INT_LEVANTAMIENTO
insert into ESTADO_INT_LEVANTAMIENTO (COD_ESTADO_INT_LEVANTAMIENTO, DES_ESTADO_INT_LEVANTAMIENTO) values (1, 'Inicial');
insert into ESTADO_INT_LEVANTAMIENTO (COD_ESTADO_INT_LEVANTAMIENTO, DES_ESTADO_INT_LEVANTAMIENTO) values (2, 'Pendiente de Contabilizar');
insert into ESTADO_INT_LEVANTAMIENTO (COD_ESTADO_INT_LEVANTAMIENTO, DES_ESTADO_INT_LEVANTAMIENTO) values (3, 'Procesada');

-- Inserts nueva tabla ESTADO_SOLICITUD_CANCELACION --> Acaba siendo la tabla ESTADO_INT_CANCELACION
insert into ESTADO_INT_CANCELACION (COD_ESTADO_INT_CANCELACION, DES_ESTADO_INT_CANCELACION) values (1, 'Inicial');
insert into ESTADO_INT_CANCELACION (COD_ESTADO_INT_CANCELACION, DES_ESTADO_INT_CANCELACION) values (2, 'Pendiente de Contabilizar');
insert into ESTADO_INT_CANCELACION (COD_ESTADO_INT_CANCELACION, DES_ESTADO_INT_CANCELACION) values (3, 'Procesada');

-- Inserts nueva tabla ESTADO_RESPUESTA_TRABA_CGPJ --> Acaba siendo la tabla ESTADO_RESP_TRABA
insert into ESTADO_RESP_TRABA (COD_ESTADO_RESP_TRABA, DES_ESTADO_RESP_TRABA) values (2000, 'Traba Procesada Totalmente');
insert into ESTADO_RESP_TRABA (COD_ESTADO_RESP_TRABA, DES_ESTADO_RESP_TRABA) values (2001, 'Traba Procesada Parcialmente');
insert into ESTADO_RESP_TRABA (COD_ESTADO_RESP_TRABA, DES_ESTADO_RESP_TRABA) values (3001, 'Cliente Sin Saldo');
insert into ESTADO_RESP_TRABA (COD_ESTADO_RESP_TRABA, DES_ESTADO_RESP_TRABA) values (3007, 'Importe no válido');
insert into ESTADO_RESP_TRABA (COD_ESTADO_RESP_TRABA, DES_ESTADO_RESP_TRABA) values (3008, 'Cliente sin saldo con otras posiciones');
insert into ESTADO_RESP_TRABA (COD_ESTADO_RESP_TRABA, DES_ESTADO_RESP_TRABA) values (3009, 'Otros tipos de error no especificados');

-- Inserts nueva tabla ESTADO_RESPUESTA_EJECUCION_CGPJ --> Acaba siendo la tabla ESTADO_RESP_EJECUCION
insert into ESTADO_RESP_EJECUCION (COD_ESTADO_RESP_EJECUCION, DES_ESTADO_RESP_EJECUCION) values (2002, 'Ejecución Procesada');
insert into ESTADO_RESP_EJECUCION (COD_ESTADO_RESP_EJECUCION, DES_ESTADO_RESP_EJECUCION) values (3005, 'Ejecución sin traba anterior');
insert into ESTADO_RESP_EJECUCION (COD_ESTADO_RESP_EJECUCION, DES_ESTADO_RESP_EJECUCION) values (3007, 'Importe no válido');
insert into ESTADO_RESP_EJECUCION (COD_ESTADO_RESP_EJECUCION, DES_ESTADO_RESP_EJECUCION) values (3009, 'Otros tipos de error no especificados');

-- Inserts nueva tabla ESTADO_RESPUESTA_LEVANTAMIENTO_CGPJ --> Acaba siendo la tabla ESTADO_RESP_LEVANTAMIENTO
insert into ESTADO_RESP_LEVANTAMIENTO (COD_ESTADO_RESP_LEVANTAMIENTO, DES_ESTADO_RESP_LEVANTAMIENTO) values (2003, 'Levantamiento Procesado');
insert into ESTADO_RESP_LEVANTAMIENTO (COD_ESTADO_RESP_LEVANTAMIENTO, DES_ESTADO_RESP_LEVANTAMIENTO) values (3006, 'Levantamiento sin traba anterior');
insert into ESTADO_RESP_LEVANTAMIENTO (COD_ESTADO_RESP_LEVANTAMIENTO, DES_ESTADO_RESP_LEVANTAMIENTO) values (3007, 'Importe no válido');
insert into ESTADO_RESP_LEVANTAMIENTO (COD_ESTADO_RESP_LEVANTAMIENTO, DES_ESTADO_RESP_LEVANTAMIENTO) values (3009, 'Otros tipos de error no especificados');

-- Inserts nueva tabla TIPO_DOCUMENTACION_CGPJ --> Acaba siendo la tabla TIPO_DOCUMENTACION
insert into TIPO_DOCUMENTACION (COD_TIPO_DOCUMENTACION, DES_TIPO_DOCUMENTACION) values (1, 'CIF');
insert into TIPO_DOCUMENTACION (COD_TIPO_DOCUMENTACION, DES_TIPO_DOCUMENTACION) values (2, 'NIF');
insert into TIPO_DOCUMENTACION (COD_TIPO_DOCUMENTACION, DES_TIPO_DOCUMENTACION) values (3, 'DNI');
insert into TIPO_DOCUMENTACION (COD_TIPO_DOCUMENTACION, DES_TIPO_DOCUMENTACION) values (4, 'Pasaporte');
insert into TIPO_DOCUMENTACION (COD_TIPO_DOCUMENTACION, DES_TIPO_DOCUMENTACION) values (5, 'NIE');

-- Comentarios en las tablas
COMMENT ON TABLE ESTADO_PRIMARIO_PETICION IS 'Tabla base para los estados de envío que manda el CGPJ a la Entidad Financiera.';
COMMENT ON TABLE ESTADO_PRIMARIO_RESP IS 'Tabla base para los estados de las respuestas que manda la Entidad Financiera al CGPJ.';
COMMENT ON TABLE ESTADO_SECUNDARIO_RESP IS 'Tabla base para los estados secundarios de las respuestas que manda la Entidad Financiera al CGPJ.';
COMMENT ON TABLE ESTADO_INT_PETICION IS 'Tabla Base para almacenar el estado interno de un fichero de peticiones.';
COMMENT ON TABLE ESTADO_INT_TRABA IS 'Tabla Base para almacenar el estado interno de una solicitud de traba del CGPJ.';
COMMENT ON TABLE ESTADO_INT_EJECUCION IS 'Tabla Base para almacenar el estado interno de una solicitud de ejecución del CGPJ.';
COMMENT ON TABLE ESTADO_INT_LEVANTAMIENTO IS 'Tabla Base para almacenar el estado interno de una solicitud de levantamiento del CGPJ.';
COMMENT ON TABLE ESTADO_INT_CANCELACION IS 'Tabla Base para almacenar el estado interno de una solicitud de cancelación del CGPJ.';
COMMENT ON TABLE ESTADO_RESP_TRABA IS 'Tabla con las posibles respuestas a las peticiones de traba.';
COMMENT ON TABLE ESTADO_RESP_EJECUCION IS 'Tabla con las posibles respuestas a las solicitudes de ejecución.';
COMMENT ON TABLE ESTADO_RESP_LEVANTAMIENTO IS 'Tabla con las posibles respuestas a las solicitudes de levantamiento.';
COMMENT ON TABLE TIPO_DOCUMENTACION IS 'Tabla con las posibles tipos de documento para identificar a un cliente.';

COMMIT;

---*** FIN INSERTS CGPJ. ***---

-- Nuevos inserts:
INSERT INTO ESTADO_CTRLFICHERO (COD_TIPO_FICHERO, COD_ESTADO, DESCRIPCION) VALUES (1, 0, 'En error');
INSERT INTO ESTADO_CTRLFICHERO (COD_TIPO_FICHERO, COD_ESTADO, DESCRIPCION) VALUES (1, 1, 'Cargando');
INSERT INTO ESTADO_CTRLFICHERO (COD_TIPO_FICHERO, COD_ESTADO, DESCRIPCION) VALUES (1, 2, 'Recibido');
INSERT INTO ESTADO_CTRLFICHERO (COD_TIPO_FICHERO, COD_ESTADO, DESCRIPCION) VALUES (1, 3, 'Tramitado');
INSERT INTO ESTADO_CTRLFICHERO (COD_TIPO_FICHERO, COD_ESTADO, DESCRIPCION) VALUES (1, 4, 'Generado');
INSERT INTO ESTADO_CTRLFICHERO (COD_TIPO_FICHERO, COD_ESTADO, DESCRIPCION) VALUES (1, 5, 'Enviado');

COMMIT;
