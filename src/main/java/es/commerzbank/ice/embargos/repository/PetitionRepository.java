package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static es.commerzbank.ice.embargos.utils.EmbargosConstants.LEVANTAMIENTO_CGPJ_ESTADO_INT_PDTE_CONTA;
import static es.commerzbank.ice.embargos.utils.EmbargosConstants.LEVANTAMIENTO_CGPJ_ESTADO_RESP_LEV;

public interface PetitionRepository
	extends JpaRepository<Peticion, String>, JpaSpecificationExecutor<Peticion>
{
	// todo delete?
	public Peticion findByControlFichero(ControlFichero controlFichero);

	/*String codSolicitud, String NIF, String iban, String tipo, BigDecimal importe, BigDecimal cambio, BigDecimal importeDivisa, String divisa
	  @Query(
      "SELECT new es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO(cial.id, cial.idJet, cial.name, cial.nif, cial.status, client.id, "
          + "client.name, agencyGroup.id, agencyGroup.name, brand.id, brand.name, market.id, translation.text, cial.activo) FROM CialInterlocutorJet cial "
          + "INNER JOIN cial.agencyGroupJet agencyGroup INNER JOIN cial.brandJet brand INNER JOIN cial.marketJet market "
          + "LEFT JOIN market.nameTranslations translation ON translation.languageId = :currentLanguageId LEFT JOIN cial.crmClientJet client ")
  Page<CialInterlocutorJetInfo> findAllProjectedForList(@Param("currentLanguageId") Long currentLanguageId,
      Pageable pageable);

      FROM CUENTA_TRABA ct
INNER JOIN TRABAS t ON t.COD_TRABA = ct.COD_TRABA
INNER JOIN EMBARGO e ON e.COD_EMBARGO = t.COD_EMBARGO
INNER JOIN SOLICITUDES_TRABA st ON st.COD_TRABA = t.COD_TRABA
WHERE ct.COD_ESTADO = 1

	 */

	@Query(
			"SELECT new es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO(st.peticion.codPeticion, st.codSolicitudTraba, ct.codCuentaTraba, e.nif, e.nombre, " +
					"ct.iban, 'TRABA', ct.importe, ct.cambio, ct.divisa) FROM " +
					"SolicitudesTraba st " +
					"INNER JOIN st.traba t " +
					"INNER JOIN t.embargo e " +
					"INNER JOIN t.cuentaTrabas ct " +
					"WHERE " +
					"ct.estadoTraba.codEstado = 1" +
					" AND t.revisado = '"+ EmbargosConstants.IND_FLAG_SI +"'" +
					" AND ct.importe > 0" +
					" AND ct.agregarATraba = '"+ EmbargosConstants.IND_FLAG_YES +"'")
	List<AccountingPendingDTO> findTrabasAccountingPending();

	@Query(
			"SELECT new es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO(sl.peticion.codPeticion, sl.codSolicitudLevantamiento, cl.codCuentaLevantamiento, e.nif, e.nombre, " +
					"cl.iban, 'LEVANTAMIENTO', cl.importe, cl.cambio, cl.codDivisa) FROM " +
					"SolicitudesLevantamiento sl " +
					"INNER JOIN sl.levantamientoTraba l " +
					"INNER JOIN l.traba t " +
					"INNER JOIN t.embargo e " +
					"INNER JOIN l.cuentaLevantamientos cl " +
					"WHERE " +
					"cl.estadoLevantamiento.codEstado = 1" +
					" AND cl.importe > 0" +
					" AND sl.estadoIntLevantamiento = "+ LEVANTAMIENTO_CGPJ_ESTADO_INT_PDTE_CONTA +
					" AND sl.estadoRespLevantamiento = "+ LEVANTAMIENTO_CGPJ_ESTADO_RESP_LEV)
	List<AccountingPendingDTO> findLevantamientosAccountingPending();
}
