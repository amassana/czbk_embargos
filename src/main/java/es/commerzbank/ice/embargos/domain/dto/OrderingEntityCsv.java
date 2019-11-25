package es.commerzbank.ice.embargos.domain.dto;

public class OrderingEntityCsv {
	private long codOrderingEntity;
	private String nifEntity;
	private String desEntity;
	private boolean indCgpj;
	private boolean indAeat;
	private boolean indNorma63;
	private boolean active;
	private Object codeCommunicatingEntity;
	private String descCommunicatingEntity;
	
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
	public boolean getIndCgpj() {
		return indCgpj;
	}
	public void setIndCgpj(boolean indCgpj) {
		this.indCgpj = indCgpj;
	}
	public boolean getIndAeat() {
		return indAeat;
	}
	public void setIndAeat(boolean indAeat) {
		this.indAeat = indAeat;
	}
	public boolean getIndNorma63() {
		return indNorma63;
	}
	public void setIndNorma63(boolean indNorma63) {
		this.indNorma63 = indNorma63;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Object getCodeCommunicatingEntity() {
		return codeCommunicatingEntity;
	}
	public void setCodeCommunicatingEntity(Object codeCommunicatingEntity) {
		this.codeCommunicatingEntity = codeCommunicatingEntity;
	}
	public String getDescCommunicatingEntity() {
		return descCommunicatingEntity;
	}
	public void setDescCommunicatingEntity(String descCommunicatingEntity) {
		this.descCommunicatingEntity = descCommunicatingEntity;
	}
	
}
