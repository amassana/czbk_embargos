package es.commerzbank.ice.embargos.service;


public interface SeizureService {

	public byte[] generateJustificanteEmbargo(Integer codEmbargo) throws Exception;
}
