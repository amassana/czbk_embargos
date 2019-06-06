package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.util.List;

public class EntidadCreditoGroup {

	private EntidadCredito entidadCredito;
	private List<Diligencia> diligenciaList;
	private FinEntidadCredito finEntidadCredito;
	
	public EntidadCredito getEntidadCredito() {
		return entidadCredito;
	}
	public void setEntidadCredito(EntidadCredito entidadCredito) {
		this.entidadCredito = entidadCredito;
	}
	public List<Diligencia> getDiligenciaList() {
		return diligenciaList;
	}
	public void setDiligenciaList(List<Diligencia> diligenciaList) {
		this.diligenciaList = diligenciaList;
	}
	public FinEntidadCredito getFinEntidadCredito() {
		return finEntidadCredito;
	}
	public void setFinEntidadCredito(FinEntidadCredito finEntidadCredito) {
		this.finEntidadCredito = finEntidadCredito;
	}
	
	
	
}
