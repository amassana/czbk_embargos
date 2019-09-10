package es.commerzbank.ice.embargos.domain.dto;

public class OrderingEntity {
	private long codOrderingEntity;
	private String nifEntity;
	private String desEntity;
	private String indCgpj;
	private String indAeat;
	private String indNorma63;
	private long transmitter;
	private boolean active;
	
	
	public long getCodOrderingEntity() {
		return codOrderingEntity;
	}
	public void setCodOrderingEntity(long codOrderingEntity) {
		this.codOrderingEntity = codOrderingEntity;
	}
	public String getNifEntity() {
		return nifEntity;
	}
	public void setNifEntity(String nifEntity) {
		this.nifEntity = nifEntity;
	}
	public String getDesEntity() {
		return desEntity;
	}
	public void setDesEntity(String desEntity) {
		this.desEntity = desEntity;
	}
	public String getIndCgpj() {
		return indCgpj;
	}
	public void setIndCgpj(String indCgpj) {
		this.indCgpj = indCgpj;
	}
	public String getIndAeat() {
		return indAeat;
	}
	public void setIndAeat(String indAeat) {
		this.indAeat = indAeat;
	}
	public String getIndNorma63() {
		return indNorma63;
	}
	public void setIndNorma63(String indNorma63) {
		this.indNorma63 = indNorma63;
	}
	public long getTransmitter() {
		return transmitter;
	}
	public void setTransmitter(long transmitter) {
		this.transmitter = transmitter;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
