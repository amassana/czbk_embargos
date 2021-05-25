package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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
      "SELECT new es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO('st.COD_PETICION', 'st.COD_SOLICITUD_TRABA', e.NIF, e.NOMBRE, " +
			  "ct.IBAN, 'TRABA', ct.IMPORTE, ct.CAMBIO, ct.IMPORTE * ct.CAMBIO, ct.DIVISA) FROM CuentaTraba ct "
          + "INNER JOIN ct.traba t INNER JOIN t.embargo e INNER JOIN cial.marketJet market "
          + "LEFT JOIN market.nameTranslations translation ON translation.languageId = :currentLanguageId LEFT JOIN cial.crmClientJet client " +
			  "WHERE ct.estadoTraba.codEstado = 1")
	  Page<AccountingPendingDTO> findAccountingPending(Pageable pageable);
}
