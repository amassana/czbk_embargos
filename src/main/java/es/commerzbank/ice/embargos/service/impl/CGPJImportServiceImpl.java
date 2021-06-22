package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.CGPJImportService;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.utils.CGPJUtils;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static es.commerzbank.ice.embargos.utils.EmbargosConstants.*;

@Service
public class CGPJImportServiceImpl
    implements CGPJImportService
{
    @Autowired
    private FileControlRepository fileControlRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SeizureRepository seizureRepository;

    @Autowired
    private SeizedRepository seizedRepository;

    @Autowired
    private LiftingRepository liftingRepository;

    @Autowired
    private SeizureBankAccountRepository seizureBankAccountRepository;

    @Autowired
    private SeizedBankAccountRepository seizedBankAccountRepository;

    @Autowired
    private LiftingBankAccountRepository liftingBankAccountRepository;

    @Autowired
    private es.commerzbank.ice.embargos.domain.mapper.CGPJMapper CGPJMapper;

    @Autowired
    private SolicitudTrabaRepository solicitudTrabaRepository;

    @Autowired
    private CuentaTrabaCGPJCopyRepository cuentaTrabaCGPJCopyRepository;

    @Override
    public List<ControlFichero> listPending() {
        return fileControlRepository.findPendingCGPJ();
    }

    @Override
    public void importCGPJ(ControlFichero controlFichero, Peticion peticion)
            throws Exception
    {
        controlFichero.setIndTieneTrabas(IND_FLAG_NO);
        controlFichero.setIndTieneLevantamientos(IND_FLAG_NO);
        controlFichero.setIndTieneEjecuciones(IND_FLAG_NO);

        importEmbargos(controlFichero);
        importLevantamientos(controlFichero, peticion);
        importEjecuciones(controlFichero, peticion);

        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
                EmbargosConstants.COD_ESTADO_CTRLFICHERO_CGPJ_IMPORTADO,
                EmbargosConstants.COD_TIPO_FICHERO_PETICION_CGPJ);

        controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);

        fileControlRepository.saveAndFlush(controlFichero);
    }

    private void importEmbargos(ControlFichero controlFichero)
        throws Exception
    {
        for (Embargo embargo : controlFichero.getEmbargos()) {
            importEmbargo(embargo);
        }

        if (controlFichero.getEmbargos().size() > 0) {
            controlFichero.setIndTieneTrabas(IND_FLAG_SI);
            controlFichero.setCodSubestadoTraba(COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_RECEIVED);
        }
    }

    private void importEmbargo(Embargo embargo)
            throws Exception
    {
        CustomerDTO customerDTO = customerService.findCustomerByNif(embargo.getNif(), false);

        if (embargo.getTrabas() == null)
            throw new Exception ("Se esperaba una traba asociada al embargo " + embargo.getCodEmbargo());

        if (embargo.getTrabas().size() != 1)
            throw new Exception ("Se han encontrado "+ embargo.getTrabas().size() +" trabas asociadas al embargo " + embargo.getCodEmbargo());

        Traba traba = embargo.getTrabas().get(0);

        embargo.setRazonSocialInterna(EmbargosUtils.determineRazonSocialInternaFromCustomer(customerDTO));

        traba.setRevisado(IND_FLAG_NO);

        int counter = 1;
        embargo.setCuentaEmbargos(new ArrayList<>());
        traba.setCuentaTrabas(new ArrayList<>());

        for (AccountDTO accountDTO : customerDTO.getBankAccounts())
        {
            CuentaEmbargo cuentaEmbargo = CGPJMapper.generateCuentaEmbargo(accountDTO, BigDecimal.valueOf(counter));
            embargo.addCuentaEmbargo(cuentaEmbargo);
            seizureBankAccountRepository.save(cuentaEmbargo);

            CuentaTraba cuentaTraba = CGPJMapper.generateCuentaTraba(accountDTO, BigDecimal.valueOf(counter));
            traba.addCuentaTraba(cuentaTraba);
            seizedBankAccountRepository.save(cuentaTraba);

            counter++;
        }

        seizureRepository.save(embargo);

        seizedRepository.save(traba);
    }

    private void importEjecuciones(ControlFichero controlFichero, Peticion peticion) {
        if (peticion.getSolicitudesEjecucions() != null && peticion.getSolicitudesEjecucions().size() > 0) {
            controlFichero.setIndTieneEjecuciones(IND_FLAG_SI);
        }
    }

    private void importLevantamientos(ControlFichero controlFichero, Peticion peticion)
        throws Exception
    {
        for (SolicitudesLevantamiento solicitudLevantamiento : peticion.getSolicitudesLevantamientos()) {
            importSolicitudLevantamiento(solicitudLevantamiento);
        }

        if (peticion.getSolicitudesLevantamientos().size() > 0) {
            controlFichero.setIndTieneLevantamientos(IND_FLAG_SI);

            // estat dependent del què indiqui la solicitud?
            controlFichero.setCodSubestadoTraba(COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_RECEIVED);

            // creación de cuenta_levantamiento PDTE
            // cambio a estado procesado cuando todas las trabas y los levantamientos han sido procesados
            // el check de revisado / incluir en traba ..?


        }
    }

    private void importSolicitudLevantamiento(SolicitudesLevantamiento solicitudLevantamiento)
        throws Exception
    {
        Traba traba = solicitudLevantamiento.getLevantamientoTraba().getTraba();

        Optional<SolicitudesTraba> optionalSolicitudesTraba = solicitudTrabaRepository.findByTraba(traba);

        if (!optionalSolicitudesTraba.isPresent())
            throw new Exception ("Se esperaba una solicitud de traba asociada a la traba " + traba.getCodTraba() +" en la solicitud "+ solicitudLevantamiento.getCodSolicitudLevantamiento());

        SolicitudesTraba solicitudTraba = optionalSolicitudesTraba.get();

        List<LevantamientoTraba> levantamientos = liftingRepository.findAllByTraba(traba);

        /* asumimos que es el primer levantamiento sin importe el que hace falta completar */

        for (LevantamientoTraba levantamiento : levantamientos) {
            if (levantamiento.getImporteLevantado() != null)
                continue;

            importLevantamiento(solicitudLevantamiento, traba, levantamiento);

            break;  // no procesamos otros levantamientos, ya está importado
        }
    }

    private void importLevantamiento(SolicitudesLevantamiento solicitudLevantamiento, Traba traba, LevantamientoTraba levantamiento)
            throws Exception
    {
        restaurarCuentaTraba(traba);
        CuentaTraba cuentaTraba = findCuentaTraba(solicitudLevantamiento, traba);

        if (cuentaTraba == null)
            throw new Exception("No se puede seleccionar una cuenta traba a vincular a la cuenta levantada");

        CuentaLevantamiento cuentaLevantamiento = CGPJMapper.generateCuentaLevantamiento(levantamiento, solicitudLevantamiento, cuentaTraba);

        liftingRepository.save(levantamiento);

        levantamiento.setImporteLevantado(CGPJUtils.parse(solicitudLevantamiento.getImporteSolicitud()));

        if (solicitudLevantamiento.getEstadoIntLevantamiento().getCodEstadoIntLevantamiento() == CGPJ_ESTADO_INTERNO_LEVANTAMIENTO_INICIAL
                || solicitudLevantamiento.getEstadoIntLevantamiento().getCodEstadoIntLevantamiento() == CGPJ_ESTADO_INTERNO_LEVANTAMIENTO_PENDIENTE_CONTABILIZAR) {
            EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
            estadoLevantamiento.setCodEstado(COD_ESTADO_LEVANTAMIENTO_PENDIENTE);
            levantamiento.setEstadoLevantamiento(estadoLevantamiento);
        }
        else {
            ; // TODO debería haber un estado levantamiento adecuado para este caso; ahora Pendiente - Pendiente de contabilización - Contabilizado
        }

        liftingBankAccountRepository.save(cuentaLevantamiento);
    }

    // El integrador parece borrar la tabla cuenta_traba al levantar. Se recupera su contenido capturado al hacer reply
    private void restaurarCuentaTraba(Traba traba) {
        List<CuentaTrabaCGPJCopy> backupCuentas = cuentaTrabaCGPJCopyRepository.findByTraba(traba);

        for (CuentaTrabaCGPJCopy backup : backupCuentas) {
            boolean found = false;
            for (CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
                if (cuentaTraba.getCuenta().equals(backup.getCuenta())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                CuentaTraba cuentaTraba = CGPJMapper.generateCuentaTraba(backup);
                seizedBankAccountRepository.insertarCuentaTraba(cuentaTraba.getCodCuentaTraba(), cuentaTraba.getTraba().getCodTraba(), cuentaTraba.getEstadoTraba().getCodEstado());
                seizedBankAccountRepository.save(cuentaTraba);
                traba.addCuentaTraba(cuentaTraba);
            }
        }
    }

    private CuentaTraba findCuentaTraba(SolicitudesLevantamiento solicitudLevantamiento, Traba traba)
        throws ParseException
    {
        BigDecimal importe = CGPJUtils.parse(solicitudLevantamiento.getImporteSolicitud());
        CuentaTraba cuentaTraba = null;

        // Primer criterio: una cuenta traba cuyo importe sea igual al importe a levantar.
        for (CuentaTraba cuentaTrabaActual : traba.getCuentaTrabas()) {
            if (IND_FLAG_YES.equals(cuentaTrabaActual.getAgregarATraba())) {
                if (importe.compareTo(cuentaTrabaActual.getImporte()) == 0) {
                    cuentaTraba = cuentaTrabaActual;
                    break;
                }
            }
        }
        // si no hay una cuenta traba con importe igual, se coge la primera con importe mayor al que se levantará
        if (cuentaTraba == null) {
            for (CuentaTraba cuentaTrabaActual : traba.getCuentaTrabas()) {
                if (IND_FLAG_YES.equals(cuentaTrabaActual.getAgregarATraba())) {
                    if (importe.compareTo(cuentaTrabaActual.getImporte()) < 0) {
                        cuentaTraba = cuentaTrabaActual;
                        break;
                    }
                }
            }
        }

        return cuentaTraba;
    }
}
