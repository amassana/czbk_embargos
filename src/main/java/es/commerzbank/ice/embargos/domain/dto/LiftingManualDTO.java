package es.commerzbank.ice.embargos.domain.dto;

import java.util.List;

public class LiftingManualDTO
{
	private CommunicatingEntity entity;
	private List<ClientLiftingManualDTO> clients;

	public List<ClientLiftingManualDTO> getClients() {
		return clients;
	}
	public void setClients(List<ClientLiftingManualDTO> clients) {
		this.clients = clients;
	}

	public CommunicatingEntity getEntity() {
		return entity;
	}

	public void setEntity(CommunicatingEntity entity) {
		this.entity = entity;
	}
}
