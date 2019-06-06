package es.commerzbank.ice.embargos.formats.aeat.validaciontrabas;

import java.util.List;

public class EntidadCreditoGroup {

	private EntidadCredito entidadCredito;
	private List<ErroresTraba> erroresTrabaList;
	private FinEntidadCredito finEntidadCredito;
	
	
	public EntidadCredito getEntidadCredito() {
		return entidadCredito;
	}
	public void setEntidadCredito(EntidadCredito entidadCredito) {
		this.entidadCredito = entidadCredito;
	}
	public List<ErroresTraba> getErroresTrabaList() {
		return erroresTrabaList;
	}
	public void setErroresTrabaList(List<ErroresTraba> erroresTrabaList) {
		this.erroresTrabaList = erroresTrabaList;
	}
	public FinEntidadCredito getFinEntidadCredito() {
		return finEntidadCredito;
	}
	public void setFinEntidadCredito(FinEntidadCredito finEntidadCredito) {
		this.finEntidadCredito = finEntidadCredito;
	}
	
	

	
}
