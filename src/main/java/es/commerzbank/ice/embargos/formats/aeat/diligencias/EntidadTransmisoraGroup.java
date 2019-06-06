package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.util.List;

public class EntidadTransmisoraGroup {

	private EntidadTransmisora entidadTransmisora;
	private List<EntidadCreditoGroup> entidadCreditoGroupList;
	private FinEntidadTransmisora finEntidadTransmisora;
	
	public EntidadTransmisora getEntidadTransmisora() {
		return entidadTransmisora;
	}
	public void setEntidadTransmisora(EntidadTransmisora entidadTransmisora) {
		this.entidadTransmisora = entidadTransmisora;
	}
	public List<EntidadCreditoGroup> getEntidadCreditoGroupList() {
		return entidadCreditoGroupList;
	}
	public void setEntidadCreditoGroupList(List<EntidadCreditoGroup> entidadCreditoGroupList) {
		this.entidadCreditoGroupList = entidadCreditoGroupList;
	}
	public FinEntidadTransmisora getFinEntidadTransmisora() {
		return finEntidadTransmisora;
	}
	public void setFinEntidadTransmisora(FinEntidadTransmisora finEntidadTransmisora) {
		this.finEntidadTransmisora = finEntidadTransmisora;
	}
	
	
}
