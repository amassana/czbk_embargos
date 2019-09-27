package es.commerzbank.ice.embargos.domain.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import es.commerzbank.ice.embargos.domain.entity.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.CabeceraEmisorFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.FinFicheroFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.SolicitudInformacionFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.CabeceraEmisorFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.FinFicheroFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.RespuestaSolicitudInformacionFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.CabeceraEmisorFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.FinFicheroFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoComplementarioFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.CabeceraEmisorFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.ComunicacionResultadoRetencionFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.FinFicheroFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.OrdenLevantamientoRetencionFase5;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class Cuaderno63Mapper {

	LevantamientoHelperMapper levantamientoHelperMapper = new LevantamientoHelperMapper();
	
	
//	@Mappings({
//		@Mapping(source = "codControlFicheroPeticion", target = "controlFichero.codControlFichero")
//	})
//	public abstract Peticion generatePeticion(Long codControlFicheroPeticion);
//
//	
//	@AfterMapping
//	protected void setPeticionAfterMapping(@MappingTarget Peticion peticion) {
//			
//		//eliminar en el futuro, debe ser por sequence cuando no sea un char(16):
//		peticion.setCodPeticion(Long.toString(System.currentTimeMillis()));
//		
//		EstadoPrimarioPeticion estadoPrimarioPeticion = new EstadoPrimarioPeticion();
//		estadoPrimarioPeticion.setCodEstadoPrimarioPeticion(EmbargosConstants.COD_ESTADO_PRIMARIO_PETICION_ENVIANDO_PETICION_UNICA);	
//		peticion.setEstadoPrimarioPeticion(estadoPrimarioPeticion);
//		
//		EstadoIntPeticion estadoIntPeticion = new EstadoIntPeticion();
//		estadoIntPeticion.setCodEstadoIntPeticion(1L);
//		peticion.setEstadoIntPeticion(estadoIntPeticion);
//		
//		peticion.setNumElementos(BigDecimal.valueOf(1L));
//		
//        //Usuario y fecha ultima modificacion:
//		peticion.setUsuarioUltModificacion(EmbargosConstants.SYSTEM_USER);
//		peticion.setFUltimaModificacion(BigDecimal.valueOf(System.currentTimeMillis()));
//		
//	}

	@Mappings({
		@Mapping(source = "solicitudInfo.nifDeudor", target = "nif"),
		@Mapping(source = "solicitudInfo.nombreDeudor", target = "razonSocial"),
		@Mapping(source = "razonSocialInterna", target = "razonSocialInterna"),
		@Mapping(source = "solicitudInfo.domicilioDeudor", target = "domicilio"),
		@Mapping(source = "solicitudInfo.identificadorDeuda", target = "numeroEmbargo"),
		@Mapping(source = "solicitudInfo.codigoDeuda", target = "codDeudaDeudor"),
		@Mapping(source = "codControlFicheroPeticion", target = "controlFichero.codControlFichero")
	})
	public abstract PeticionInformacion generatePeticionInformacion(SolicitudInformacionFase1 solicitudInfo, 
			Long codControlFicheroPeticion, List<AccountDTO> listBankAccount, String razonSocialInterna);
	
	@AfterMapping
	protected void setPeticionInformacionAfterMapping(@MappingTarget PeticionInformacion peticionInformacion, List<AccountDTO> listBankAccount) {
		
		//Variables pendientes de cambiar:
		BigDecimal pendienteCambiarBigDec = new BigDecimal(0);
		EntidadesOrdenante pendienteCambiarEntidadesOrdenante  = new EntidadesOrdenante();
		pendienteCambiarEntidadesOrdenante.setCodEntidadOrdenante(Long.valueOf(1));
				
		peticionInformacion.setCodSucursal(pendienteCambiarBigDec);
		peticionInformacion.setEntidadesOrdenante(pendienteCambiarEntidadesOrdenante);
		
		//Se guardaran las primeras cuentas en estado NORMAL del listado de cuentas, hasta un maximo de 6 cuentas:
		setPreloadedBankAccounts(peticionInformacion,listBankAccount);
		
        //Usuario y fecha ultima modificacion:
		peticionInformacion.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
        peticionInformacion.setFUltimaModificacion(BigDecimal.valueOf(System.currentTimeMillis()));
		
	}
	
	/**
	 * Setea las primeras cuentas en estado NORMAL del listado de cuentas 'listAccount', hasta un maximo de seis cuentas.
	 * 
	 * @param peticionInformacion
	 * @param listAccount
	 */
	private void setPreloadedBankAccounts(PeticionInformacion peticionInformacion, List<AccountDTO> listAccount) {
	
		//Iterating by the bank accounts:
		int i=1;
		for (AccountDTO accountDTO : listAccount) {
				
			//Solo se setean las cuentas que tengan estado ACTIVE:
			if (EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE.equals(accountDTO.getStatus())) {
				switch(i) {
					case 1:
						peticionInformacion.setCuenta1(accountDTO.getAccountNum());
						peticionInformacion.setIban1(accountDTO.getIban());
						break;
					case 2:
						peticionInformacion.setCuenta2(accountDTO.getAccountNum());
						peticionInformacion.setIban2(accountDTO.getIban());
						break;
					case 3:
						peticionInformacion.setCuenta3(accountDTO.getAccountNum());
						peticionInformacion.setIban3(accountDTO.getIban());
						break;
					case 4:
						peticionInformacion.setCuenta4(accountDTO.getAccountNum());
						peticionInformacion.setIban4(accountDTO.getIban());
						break;
					case 5:
						peticionInformacion.setCuenta5(accountDTO.getAccountNum());
						peticionInformacion.setIban5(accountDTO.getIban());
						break;
					case 6:
						peticionInformacion.setCuenta6(accountDTO.getAccountNum());
						peticionInformacion.setIban6(accountDTO.getIban());
						break;
					default:
				}
				i++;
			}
		}
	}
	
	public abstract CabeceraEmisorFase2 generateCabeceraEmisorFase2(CabeceraEmisorFase1 cabeceraEmisorFase1, Date fechaObtencionFicheroEntidadDeDeposito);

	@Mappings({
		@Mapping(source = "solicitudInformacionFase1.codigoRegistro", target = "codigoRegistro"),
		@Mapping(source = "solicitudInformacionFase1.nifDeudor", target = "nifDeudor"),
		@Mapping(source = "solicitudInformacionFase1.nombreDeudor", target = "nombreDeudor"),
		@Mapping(source = "solicitudInformacionFase1.domicilioDeudor", target = "domicilioDeudor"),
		@Mapping(source = "solicitudInformacionFase1.municipio", target = "municipio"),
		@Mapping(source = "solicitudInformacionFase1.codigoPostal", target = "codigoPostal"),
		@Mapping(source = "solicitudInformacionFase1.identificadorDeuda", target = "identificadorDeuda"),
		@Mapping(source = "solicitudInformacionFase1.codigoDeuda", target = "codigoDeuda"),
		@Mapping(source = "peticionInformacion.iban1", target = "ibanCuenta1"),
		@Mapping(source = "peticionInformacion.iban2", target = "ibanCuenta2"),
		@Mapping(source = "peticionInformacion.iban3", target = "ibanCuenta3"),
		@Mapping(source = "peticionInformacion.iban4", target = "ibanCuenta4"),
		@Mapping(source = "peticionInformacion.iban5", target = "ibanCuenta5"),
		@Mapping(source = "peticionInformacion.iban6", target = "ibanCuenta6"),
		@Mapping(source = "peticionInformacion.claveSeguridad1", target = "claveSeguridadIban1"),
		@Mapping(source = "peticionInformacion.claveSeguridad2", target = "claveSeguridadIban2"),
		@Mapping(source = "peticionInformacion.claveSeguridad3", target = "claveSeguridadIban3"),
		@Mapping(source = "peticionInformacion.claveSeguridad4", target = "claveSeguridadIban4"),
		@Mapping(source = "peticionInformacion.claveSeguridad5", target = "claveSeguridadIban5"),
		@Mapping(source = "peticionInformacion.claveSeguridad6", target = "claveSeguridadIban6")
	})
	public abstract RespuestaSolicitudInformacionFase2 generateRespuestaSolicitudInformacionFase2(SolicitudInformacionFase1 solicitudInformacionFase1,
			PeticionInformacion peticionInformacion);
	
	public abstract FinFicheroFase2 generateFinFicheroFase2(FinFicheroFase1 finFicheroFase1); 
	
	@Mappings({
		@Mapping(source = "ordenEjecucionEmbargo.nifDeudor", target = "nif"),
		@Mapping(source = "ordenEjecucionEmbargo.nombreDeudor", target = "nombre"),
		@Mapping(source = "ordenEjecucionEmbargo.domicilioDeudor", target = "domicilio"),
		@Mapping(source = "ordenEjecucionEmbargo.municipio", target = "municipio"),
		@Mapping(source = "ordenEjecucionEmbargo.codigoPostal", target = "codigoPostal"),
		@Mapping(source = "ordenEjecucionEmbargo.identificadorDeuda", target = "numeroEmbargo"),
		@Mapping(source = "ordenEjecucionEmbargo.importeTotalAEmbargar", target = "importe"),
		@Mapping(source = "ordenEjecucionEmbargo.codigoDeuda", target = "codDeudaDeudor"),
		@Mapping(source = "codControlFicheroEmbargo", target = "controlFichero.codControlFichero"),
		@Mapping(source = "entidadOrdenante", target = "entidadesOrdenante"),
		@Mapping(source = "razonSocialInterna", target = "razonSocialInterna"),
		@Mapping(source = "fechaLimiteTraba", target = "fechaLimiteTraba")
	})
	public abstract Embargo generateEmbargo(OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, 
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, String razonSocialInterna, BigDecimal fechaLimiteTraba,  Map<String, AccountDTO> customerAccountsMap);
	
	@AfterMapping
	public void generateEmbargoAfterMapping(@MappingTarget Embargo embargo, OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, 
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp,  Map<String, AccountDTO> customerAccountsMap) {
		
		List<CuentaEmbargo> cuentaEmbargosList = new ArrayList<>();
		
		BigDecimal numeroOrden = new BigDecimal(1);
		
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;
		
		//seteo de datregcomdet:
		StringBuilder datregcomdet = new StringBuilder();
		datregcomdet.append(ordenEjecucionEmbargoComp.getTextoLibre1());
		datregcomdet.append(EmbargosConstants.SEPARADOR_ESPACIO);
		datregcomdet.append(ordenEjecucionEmbargoComp.getTextoLibre2());
		datregcomdet.append(EmbargosConstants.SEPARADOR_ESPACIO);
		datregcomdet.append(ordenEjecucionEmbargoComp.getTextoLibre3());
		
		embargo.setDatregcomdet(datregcomdet.toString());
				
		
		//SETEO DE CUENTAS:
		//Datos iban1:
		CuentaEmbargo cuentaEmbargo = null;
		
		if (ordenEjecucionEmbargo.getIbanCuenta1()!=null && !ordenEjecucionEmbargo.getIbanCuenta1().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta1());

			String claveSeguridadIban1 = ordenEjecucionEmbargo.getClaveSeguridadIban1();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban1, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}

		//Datos iban2:
		if (ordenEjecucionEmbargo.getIbanCuenta2()!=null && !ordenEjecucionEmbargo.getIbanCuenta2().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta2());

			String claveSeguridadIban2 = ordenEjecucionEmbargo.getClaveSeguridadIban2();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban2, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban3:
		if (ordenEjecucionEmbargo.getIbanCuenta3()!=null && !ordenEjecucionEmbargo.getIbanCuenta3().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta3());

			String claveSeguridadIban3 = ordenEjecucionEmbargo.getClaveSeguridadIban3();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban3, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban4:
		if (ordenEjecucionEmbargo.getIbanCuenta4()!=null && !ordenEjecucionEmbargo.getIbanCuenta4().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta4());

			String claveSeguridadIban4 = ordenEjecucionEmbargo.getClaveSeguridadIban4();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban4, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban5:
		if (ordenEjecucionEmbargo.getIbanCuenta5()!=null && !ordenEjecucionEmbargo.getIbanCuenta5().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta5());

			String claveSeguridadIban5 = ordenEjecucionEmbargo.getClaveSeguridadIban5();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban5, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban6:
		if (ordenEjecucionEmbargo.getIbanCuenta6()!=null && !ordenEjecucionEmbargo.getIbanCuenta6().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta6());

			String claveSeguridadIban6 = ordenEjecucionEmbargo.getClaveSeguridadIban6();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban6, fechaUltmaModif, usuarioModif);
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		embargo.setCuentaEmbargos(cuentaEmbargosList);
		
        //Usuario y fecha ultima modificacion:
		embargo.setUsuarioUltModificacion(usuarioModif);
		embargo.setFUltimaModificacion(fechaUltmaModif);
		
	}
	
	
	private CuentaEmbargo setCuentaEmbargoFromAccountDTO(AccountDTO accountDTO, Embargo embargo, 
			BigDecimal numeroOrden, String claveSeguridadIban, BigDecimal fechaUltmaModif, String usuarioModif) {
		
		CuentaEmbargo cuentaEmbargo = new CuentaEmbargo();
		
		if(accountDTO!=null) {
			cuentaEmbargo.setCuenta(accountDTO.getAccountNum());
			cuentaEmbargo.setIban(accountDTO.getIban());
		} else {
			//Sino, si la cuenta no se encuentra en DWH: indicar motivo de cuenta embargo a inexistente
			cuentaEmbargo.setActuacion(EmbargosConstants.CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_NORMA63);
		}

		cuentaEmbargo.setClaveSeguridad(claveSeguridadIban);
		cuentaEmbargo.setEmbargo(embargo);
		cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);

		cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
		cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
		
		return cuentaEmbargo;
	}

	@Mappings({
		@Mapping(source = "fechaLimiteTraba", target = "fechaLimite"),
	})
	public abstract Traba generateTraba(OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp,
			 Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, BigDecimal fechaLimiteTraba, Map<String, AccountDTO> customerAccountsMap);



	@AfterMapping
	public void generateTrabaAfterMapping(@MappingTarget Traba traba, OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, 
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp, EntidadesOrdenante entidadOrdenante, Map<String, AccountDTO> customerAccountsMap) {
		
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;

		/*
		MAPEO ELIMINADO
		CuentasRecaudacion cuentaRecaudacion = entidadOrdenante.getEntidadesComunicadora().getCuentasRecaudacion();	
		traba.setCuentasRecaudacion(cuentaRecaudacion);
		*/

		//TODO revisar --> Cuenta de inmovilizacion: la asignada por defecto (sucursal de Madrid con codigo=1):
		CuentasInmovilizacion cuentaInmovilizacion = new CuentasInmovilizacion();
		cuentaInmovilizacion.setCodCuentaInmovilizacion(EmbargosConstants.COD_CUENTA_INMOVILIZACION_DEFAULT);
		traba.setCuentasInmovilizacion(cuentaInmovilizacion);
		
		//Estado inicial de la traba al generarse:
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
		traba.setEstadoTraba(estadoTraba);
		
		
		//CUENTAS TRABAS:
		
		List<CuentaTraba> cuentaTrabasList = new ArrayList<>();
		
		BigDecimal numeroOrden = new BigDecimal(1);
		
		//Datos iban1:
		CuentaTraba cuentaTraba = null;
		
		if (ordenEjecucionEmbargo.getIbanCuenta1()!=null && !ordenEjecucionEmbargo.getIbanCuenta1().isEmpty()) {
			
			String iban1FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta1();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban1FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban1FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}

		//Datos iban2:
		if (ordenEjecucionEmbargo.getIbanCuenta2()!=null && !ordenEjecucionEmbargo.getIbanCuenta2().isEmpty()) {

			String iban2FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta2();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban2FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban2FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban3:
		if (ordenEjecucionEmbargo.getIbanCuenta3()!=null && !ordenEjecucionEmbargo.getIbanCuenta3().isEmpty()) {

			String iban3FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta3();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban3FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban3FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban4:
		if (ordenEjecucionEmbargo.getIbanCuenta4()!=null && !ordenEjecucionEmbargo.getIbanCuenta4().isEmpty()) {

			String iban4FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta4();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban4FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban4FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban5:
		if (ordenEjecucionEmbargo.getIbanCuenta5()!=null && !ordenEjecucionEmbargo.getIbanCuenta5().isEmpty()) {

			String iban5FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta5();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban5FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban5FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban6:
		if (ordenEjecucionEmbargo.getIbanCuenta6()!=null && !ordenEjecucionEmbargo.getIbanCuenta6().isEmpty()) {
			
			String iban6FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta6();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban6FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban6FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);

			cuentaTrabasList.add(cuentaTraba);
		}
		
		traba.setCuentaTrabas(cuentaTrabasList);
		
        //Usuario y fecha ultima modificacion:
		traba.setUsuarioUltModificacion(usuarioModif);
		traba.setFUltimaModificacion(fechaUltmaModif);
	}
	
	private CuentaTraba setCuentaTrabaFromAccountDTO(AccountDTO accountDTO, Traba traba,
			BigDecimal numeroOrden, String ibanFromOrdenEjecucionEmbargo,
			BigDecimal fechaUltmaModif, String usuarioModif) {
	
		CuentaTraba cuentaTraba = new CuentaTraba();
		
		if(accountDTO!=null) {
			
			cuentaTraba.setIban(accountDTO.getIban());
			cuentaTraba.setCuenta(accountDTO.getAccountNum());
			cuentaTraba.setDivisa(accountDTO.getDivisa());
			cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
		
		} else {
			//Sino, si la cuenta no se encuentra en DWH: indicar la actuacion (motivo) de la cuentaTraba a inexistente:
			CuentaTrabaActuacion cuentaTrabaActuacion = new CuentaTrabaActuacion();
			cuentaTrabaActuacion.setCodActuacion(EmbargosConstants.CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_NORMA63);
			cuentaTraba.setCuentaTrabaActuacion(cuentaTrabaActuacion);
			
			//El estado de la cuenta se setea a no encontrada:
			cuentaTraba.setEstadoCuenta(EmbargosConstants.BANK_ACCOUNT_STATUS_NOTFOUND);

			//Iban obtenido del fichero de embargos:
			cuentaTraba.setIban(ibanFromOrdenEjecucionEmbargo);
		}
		
		
		cuentaTraba.setTraba(traba);
		cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
		cuentaTraba.setEstadoTraba(estadoTraba);
		cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
		cuentaTraba.setUsuarioUltModificacion(usuarioModif);
		cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
		
		return cuentaTraba;
		
	}
	
	
	public abstract CabeceraEmisorFase4 generateCabeceraEmisorFase4(CabeceraEmisorFase3 cabeceraEmisorFase3);
	
	@AfterMapping
	public void setCabeceraEmisorFase4AfterMapping(@MappingTarget CabeceraEmisorFase4 cabeceraEmisorFase4) {
	
		cabeceraEmisorFase4.setFase(EmbargosConstants.COD_FASE_4);
	}
	
	@Mappings({
		@Mapping(source = "embargo.nif", target = "nifDeudor"),
		@Mapping(source = "embargo.nombre", target = "nombreDeudor"),
		@Mapping(source = "embargo.domicilio", target = "domicilioDeudor"),
		@Mapping(source = "embargo.municipio", target = "municipio"),
		@Mapping(source = "embargo.codigoPostal", target = "codigoPostal"),
		@Mapping(source = "embargo.numeroEmbargo", target = "identificadorDeuda"),
		@Mapping(source = "embargo.importe", target = "importeTotalAEmbargar"),
		@Mapping(source = "embargo.codDeudaDeudor", target = "codigoDeuda"),
		@Mapping(source = "traba.importeTrabado", target = "importeTotalRetencionesEfectuadas"),
	})
	public abstract ComunicacionResultadoRetencionFase4 generateComunicacionResultadoRetencionFase4(Embargo embargo, Traba traba, List<CuentaTraba> cuentaTrabaOrderedList);
	
	@AfterMapping
	public void setComunicacionResultadoRetencionFase4AfterMapping(@MappingTarget ComunicacionResultadoRetencionFase4 comunicacionResultadoRetencionFase4,
			Embargo embargo, Traba traba, List<CuentaTraba> cuentaTrabaOrderedList) {
		
		//Obligatorio setear el 'codigo de registro' en el registro de Comunicacion del Resultado de la Retencion:
		comunicacionResultadoRetencionFase4.setCodigoRegistro(EmbargosConstants.CODIGO_REGISTRO_CUADERNO63_COMUNICACION_RESULTADO_RETENCION_FASE4);
		
		//TODO: Si en el mapeo el domicilio es nulo, lo generamos con los campos relacionados al domicilio (calle, puerta, etc.)

		//TODO: falta setear --> fechaEjecucionRetenciones
		
		for (CuentaTraba cuentaTraba : cuentaTrabaOrderedList) {
			
			int cont = 1;
			
			boolean agregarATraba = cuentaTraba.getAgregarATraba() != null && EmbargosConstants.IND_FLAG_YES.equals(cuentaTraba.getAgregarATraba());
			
			if (agregarATraba) {
			
				String claveSeguridad = EmbargosUtils.generateClaveSeguridad(cuentaTraba.getIban());
				
				String resultadoRetencion = cuentaTraba.getCuentaTrabaActuacion() != null ? 
						cuentaTraba.getCuentaTrabaActuacion().getCodExternoActuacion() : null;
				
				switch(cont) {
					
					case 1:
						comunicacionResultadoRetencionFase4.setIbanCuenta1(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban1(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta1(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta1(resultadoRetencion);
						break;
					case 2:
						comunicacionResultadoRetencionFase4.setIbanCuenta2(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban2(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta2(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta2(resultadoRetencion);
						break;
					case 3:
						comunicacionResultadoRetencionFase4.setIbanCuenta3(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban3(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta3(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta3(resultadoRetencion);
						break;
					case 4:
						comunicacionResultadoRetencionFase4.setIbanCuenta4(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban4(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta4(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta4(resultadoRetencion);
						break;
					case 5:
						comunicacionResultadoRetencionFase4.setIbanCuenta5(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban5(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta5(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta5(resultadoRetencion);
						break;
					case 6:
						comunicacionResultadoRetencionFase4.setIbanCuenta6(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban6(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta6(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta6(resultadoRetencion);
						break;
					default:
					
				}
				cont++;
			}
		}
	}
	
	@Mappings({
		@Mapping(source = "numeroRegistrosFichero", target = "numeroRegistrosFichero"),
	})
	public abstract FinFicheroFase4 generateFinFicheroFase4(FinFicheroFase3 finFicheroFase3, Integer numeroRegistrosFichero);
	
	@AfterMapping
	public void setFinFicheroFase4AfterMapping(@MappingTarget FinFicheroFase4 finFicheroFase4) {
	
		//TODO: eliminar este metodo si queda finalmente vacio.
	}

	
	@Mappings({ @Mapping(source = "codControlFichero", target = "controlFichero.codControlFichero") })
	public abstract LevantamientoTraba generateLevantamiento(Long codControlFichero,
			OrdenLevantamientoRetencionFase5 ordenLevantamientoRetencionFase5, Traba traba, CustomerDTO DWHCustomer);

	@AfterMapping
	public void generateLevantamientoAfterMapping(@MappingTarget LevantamientoTraba levantamiento,
			OrdenLevantamientoRetencionFase5 ordenLevantamientoRetencionFase5, Traba traba, CustomerDTO DWHCustomer) {
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;

		levantamiento.setUsuarioUltModificacion(usuarioModif);
		levantamiento.setFUltimaModificacion(fechaUltmaModif);
		levantamiento.setTraba(traba);

		EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
		estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE);
		levantamiento.setEstadoLevantamiento(estadoLevantamiento);
		// TODO TO BE DEFINED
		levantamiento.setEstadoEjecutado(BigDecimal.ZERO);
		levantamiento.setEstadoContable(BigDecimal.ZERO);
		levantamiento.setIndCasoRevisado(EmbargosConstants.IND_FLAG_NO);
		// sempre a null levantamiento.setCodDeudaDeudor();

		List<CuentaLevantamiento> cuentas = new ArrayList<>(6);
		levantamiento.setCuentaLevantamientos(cuentas);

		if (ordenLevantamientoRetencionFase5.getIbanCuenta1() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta1().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento1 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta1(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban1(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif);
			cuentaLevantamiento1.setNumeroOrdenCuenta(new BigDecimal(1));
			cuentas.add(cuentaLevantamiento1);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta2() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta2().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento2 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta2(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban2(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif);
			cuentaLevantamiento2.setNumeroOrdenCuenta(new BigDecimal(2));
			cuentas.add(cuentaLevantamiento2);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta3() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta3().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento3 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta3(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban3(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif);
			cuentaLevantamiento3.setNumeroOrdenCuenta(new BigDecimal(3));
			cuentas.add(cuentaLevantamiento3);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta4() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta4().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento4 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta4(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban4(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif);
			cuentaLevantamiento4.setNumeroOrdenCuenta(new BigDecimal(4));
			cuentas.add(cuentaLevantamiento4);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta5() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta5().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento5 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta5(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban5(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif);
			cuentaLevantamiento5.setNumeroOrdenCuenta(new BigDecimal(5));
			cuentas.add(cuentaLevantamiento5);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta6() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta6().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento6 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta6(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban6(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif);
			cuentaLevantamiento6.setNumeroOrdenCuenta(new BigDecimal(6));
			cuentas.add(cuentaLevantamiento6);
		}

		BigDecimal importeLevantado = BigDecimal.ZERO;

		for (CuentaLevantamiento cuentaLevantamiento : cuentas)
		{
			if (cuentaLevantamiento.getImporte() != null)
				importeLevantado = importeLevantado.add(cuentaLevantamiento.getImporte());
		}

		levantamiento.setImporteLevantado(importeLevantado);
	}

	
}
