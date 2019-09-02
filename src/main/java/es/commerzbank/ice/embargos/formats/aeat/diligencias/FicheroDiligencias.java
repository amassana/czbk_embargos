package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.util.List;

public class FicheroDiligencias {

	private List<EntidadTransmisoraGroup> entidadTransmisoraGroupList;
	private RegistroControlEntidadTransmisora registroControlEntidadTransmisora;
	
	public List<EntidadTransmisoraGroup> getEntidadTransmisoraGroupList() {
		return entidadTransmisoraGroupList;
	}
	public void setEntidadTransmisoraGroupList(List<EntidadTransmisoraGroup> entidadTransmisoraGroupList) {
		this.entidadTransmisoraGroupList = entidadTransmisoraGroupList;
	}
	public RegistroControlEntidadTransmisora getRegistroControlEntidadTransmisora() {
		return registroControlEntidadTransmisora;
	}
	public void setRegistroControlEntidadTransmisora(RegistroControlEntidadTransmisora registroControlEntidadTransmisora) {
		this.registroControlEntidadTransmisora = registroControlEntidadTransmisora;
	}
	
	
}
