package es.commerzbank.ice.embargos.service;


public interface SeizureService {

	public byte[] generateJustificanteEmbargo(Integer idSeizure) throws Exception;
	public byte[] generarResumenTrabas(Integer codControlFichero) throws Exception;
}
