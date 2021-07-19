CREATE OR REPLACE FUNCTION "FORMATO_CCC" (cCuentaFmtCommerz Varchar2, cTerminacion Varchar2) RETURN VARCHAR2 is

cCuentaFmtCCC VARCHAR2(23);
BEGIN

  IF SUBSTR(cCuentaFmtCommerz,1,3) = '170' THEN
    cCuentaFmtCCC := '0159 0001 ** ' || SUBSTR(cCuentaFmtCommerz,4,7) || SUBSTR(cTerminacion,2,3);
  ELSE
    cCuentaFmtCCC := '0159 0002 ** ' || SUBSTR(cCuentaFmtCommerz,4,7) || SUBSTR(cTerminacion,2,3);
  END IF;

  return(cCuentaFmtCCC);

end;
