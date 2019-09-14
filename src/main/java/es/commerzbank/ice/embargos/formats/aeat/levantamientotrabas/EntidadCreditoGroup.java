package es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas;

import java.util.List;

public class EntidadCreditoGroup {

	private EntidadCredito entidadCredito;
	private List<Levantamiento> levantamientoList;
	private FinEntidadCredito finEntidadCredito;
	
	
	public EntidadCredito getEntidadCredito() {
		return entidadCredito;
	}
	public void setEntidadCredito(EntidadCredito entidadCredito) {
		this.entidadCredito = entidadCredito;
	}
	public List<Levantamiento> getLevantamientoList() {
		return levantamientoList;
	}
	public void setLevantamientoList(List<Levantamiento> levantamientoList) {
		this.levantamientoList = levantamientoList;
	}
	public FinEntidadCredito getFinEntidadCredito() {
		return finEntidadCredito;
	}
	public void setFinEntidadCredito(FinEntidadCredito finEntidadCredito) {
		this.finEntidadCredito = finEntidadCredito;
	}
		
	
}
