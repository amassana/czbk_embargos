package es.commerzbank.ice.embargos.service;

import java.math.BigDecimal;
import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

public interface FinalResponseService {

	List<FinalResponseDTO> getAllByControlFichero(ControlFichero controlFichero);

	FinalResponseDTO AddBankAccountList(Long codeFileControl, Long codeFinalResponse);
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception;
	public byte[] generarRespuestaFinalEmbargo(Integer cod_file_control) throws Exception;
	public byte[] generatePaymentLetterCGPJ(String cod_traba) throws Exception;
	public byte[] generatePaymentLetterN63(String cod_control_fichero) throws Exception;
}
