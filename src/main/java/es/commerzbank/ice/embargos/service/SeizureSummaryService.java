package es.commerzbank.ice.embargos.service;

public interface SeizureSummaryService {

	public byte[] generateSeizureSummaryReport(String accountNumber) throws Exception;
}
