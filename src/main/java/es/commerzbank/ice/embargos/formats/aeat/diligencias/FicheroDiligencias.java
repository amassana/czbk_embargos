package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.util.List;

public class FicheroDiligencias {

	private List<EntidadTransmisoraGroup> entidadTransmisoraGorupList;
	private RegistroControlEntidadTransmisora registroControlEntidadTransmisora;
	
	public List<EntidadTransmisoraGroup> getEntidadTransmisoraGorupList() {
		return entidadTransmisoraGorupList;
	}
	public void setEntidadTransmisoraGorupList(List<EntidadTransmisoraGroup> entidadTransmisoraGorupList) {
		this.entidadTransmisoraGorupList = entidadTransmisoraGorupList;
	}
	public RegistroControlEntidadTransmisora getRegistroControlEntidadTransmisora() {
		return registroControlEntidadTransmisora;
	}
	public void setRegistroControlEntidadTransmisora(RegistroControlEntidadTransmisora registroControlEntidadTransmisora) {
		this.registroControlEntidadTransmisora = registroControlEntidadTransmisora;
	}
	
	
}
