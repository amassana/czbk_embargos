package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.dto.FinalResponsePendingDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;

import java.math.BigDecimal;
import java.util.List;

public interface FinalResponseService {

	List<FinalResponseDTO> getAllByControlFichero(ControlFichero controlFichero);

	FinalResponseDTO AddBankAccountList(Long codeFileControl, Long codeFinalResponse);

	byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception;
	byte[] generarRespuestaFinalEmbargo(Integer cod_file_control) throws Exception;
	byte[] generatePaymentLetterCGPJ(String cod_traba) throws Exception;
	byte[] generatePaymentLetterN63(String cod_control_fichero) throws Exception;

	boolean updateFinalFileAccountingStatus(FicheroFinal ficheroFinal, Long codEstadoContabilizacion, String userName);

	void calcFinalResult(Long codeFileControlFase3, String user) throws Exception;
	List<FinalResponsePendingDTO> listPendingCyclesNorma63() throws Exception;
}
