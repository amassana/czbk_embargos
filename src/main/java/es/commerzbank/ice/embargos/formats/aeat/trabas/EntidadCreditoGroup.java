package es.commerzbank.ice.embargos.formats.aeat.trabas;

import java.util.List;

public class EntidadCreditoGroup {

	private EntidadCredito entidadCredito;
	private List<Traba> trabaList;
	private FinEntidadCredito finEntidadCredito;
	
	
	public EntidadCredito getEntidadCredito() {
		return entidadCredito;
	}
	public void setEntidadCredito(EntidadCredito entidadCredito) {
		this.entidadCredito = entidadCredito;
	}
	public List<Traba> getTrabaList() {
		return trabaList;
	}
	public void setTrabaList(List<Traba> trabaList) {
		this.trabaList = trabaList;
	}
	public FinEntidadCredito getFinEntidadCredito() {
		return finEntidadCredito;
	}
	public void setFinEntidadCredito(FinEntidadCredito finEntidadCredito) {
		this.finEntidadCredito = finEntidadCredito;
	}
		
	
}
