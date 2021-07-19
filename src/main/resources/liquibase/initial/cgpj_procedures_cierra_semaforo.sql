CREATE OR REPLACE FUNCTION CIERRA_SEMAFORO(
      usuario                IN VARCHAR2,
      nombre_maquina         IN VARCHAR2,
      maximo_minutos_proceso IN NUMBER)
    RETURN NUMBER
  IS
    PRAGMA AUTONOMOUS_TRANSACTION;
    cFechaSemaforo VARCHAR2(20);
    FechaSemaforo DATE;
    NumeroSemaforos  NUMBER;
    CreaSemaforo     BOOLEAN;
    v_otro_corriendo BOOLEAN;
    v_sid            NUMBER;
  BEGIN
    --Recordar dar permisos al usuario EMBARGOS2 de select sobre la tabla v$session: GRANT SELECT ON v$session TO EMBARGOS2
    --Primero de todo verificamos que ningún otro usuario ha lanzado esta función si es así le echamos
    v_sid           := NULL;
    v_otro_corriendo:=true;
    BEGIN
      SELECT sid
      INTO v_sid
      FROM v$session
      WHERE module = 'CIERRA_SEMAFORO'
      AND rownum   = 1;
    EXCEPTION
    WHEN no_data_found THEN
      v_otro_corriendo := false;
    END;
    IF v_otro_corriendo THEN
      DBMS_OUTPUT.PUT_LINE('Ya se está ejecutando este proceso');
      RETURN 0;
    END IF;
    DBMS_APPLICATION_INFO.SET_MODULE('CIERRA_SEMAFORO', 'EJECUTANDO_CIERRA_SEMAFORO');
    --DBMS_LOCK.sleep(60); --GRANT EXECUTE ON DBMS_LOCK TO EMBARGOS2
    CreaSemaforo :=false;
    BEGIN
      SELECT DD_MM_AAAA_HH_MM_SS(FECHA)
      INTO cFechaSemaforo
      FROM SEMAFORO
      WHERE ROWNUM=1;
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
      CreaSemaforo:=true;
    END;
    --Verificamos que el semáforo no lleva mucho tiempo cerrado
    FechaSemaforo:=TO_DATE(cFechaSemaforo,'dd/MM/yyyy hh24:mi:ss');
    IF ((SysDate                 -fechasemaforo)*24*60)>maximo_minutos_proceso THEN
      CreaSemaforo                                    :=true;
    END IF;
    IF CreaSemaforo THEN
      DELETE FROM SEMAFORO;
      INSERT
      INTO SEMAFORO
        (
          FECHA,
          USUARIO,
          MAQUINA
        )
        VALUES
        (
          TO_NUMBER(TO_CHAR(SysDate,'yyyyMMddhh24miss')),
          usuario,
          nombre_maquina
        );
      COMMIT;
      DBMS_APPLICATION_INFO.SET_MODULE('', '');
      RETURN 1;
    ELSE
      DBMS_APPLICATION_INFO.SET_MODULE('', '');
      RETURN 0;
    END IF;
END;
