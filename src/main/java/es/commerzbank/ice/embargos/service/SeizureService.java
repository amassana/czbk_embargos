package es.commerzbank.ice.embargos.service;

import java.math.BigDecimal;

public interface SeizureService {

	public byte[] generateJustificanteEmbargo(Integer idSeizure) throws Exception;
	public byte[] generateLevantamientoReport(Integer idLifting) throws Exception;
	public byte[] generarResumenTrabasF3(Integer codControlFichero) throws Exception;
	public byte[] generarResumenTrabasF4(Integer codControlFichero) throws Exception;
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception;
}
