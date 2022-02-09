package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.dto.FinalResponsePendingDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;

import java.util.List;

public interface FinalResponseService {

	List<FinalResponseDTO> getAllByControlFichero(ControlFichero controlFichero);

	FinalResponseDTO AddBankAccountList(Long codeFileControl, Long codeFinalResponse);

	//byte[] generarAnexo(Long codeFileControlFaseBigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception;
	byte[] generarAnexo(Long codeFileControl, Long codRepresentative) throws Exception;
	byte[] generarRespuestaFinalEmbargo(Integer codControlFichero, String oficina) throws Exception;
	byte[] generatePaymentLetterN63(String cod_control_fichero) throws Exception;

	boolean updateFinalFileAccountingStatus(FicheroFinal ficheroFinal, Long codEstadoContabilizacion, String userName);

	Long calcFinalResult(Long codeFileControlFase3, String user) throws Exception;
	List<FinalResponsePendingDTO> listPendingCyclesNorma63() throws Exception;

	boolean sendEmbargosAsTaxes(String authorization, String user);

    List<FicheroFinal> listPendingAccounting();
}
