package es.commerzbank.ice.embargos.domain.dto;

import java.util.List;

public class LiftingManualDTO {

	private String sender;
	private String nif;
	private String ine;
	private List<ClientLiftingManualDTO> clients;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getIne() {
		return ine;
	}
	public void setIne(String ine) {
		this.ine = ine;
	}
	public List<ClientLiftingManualDTO> getClients() {
		return clients;
	}
	public void setClients(List<ClientLiftingManualDTO> clients) {
		this.clients = clients;
	}
}
