package es.commerzbank.ice.embargos.service;

import java.math.BigDecimal;

public interface SeizureService {

	public byte[] generateJustificanteEmbargo(Integer idSeizure) throws Exception;
	public byte[] generarResumenTrabas(Integer codControlFichero) throws Exception;
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba) throws Exception;
}
