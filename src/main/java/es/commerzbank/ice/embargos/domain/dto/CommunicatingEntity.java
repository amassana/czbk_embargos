package es.commerzbank.ice.embargos.domain.dto;

public class CommunicatingEntity {
	private long codCommunicatingEntity;
	private String bic;
	private String iban;
	private String account;
	private boolean active;
	private String nif;
	private String name;
	private String filePrefix;
	private int responseDaysF1;
	private int responseDaysF3;
	private int responseDaysF6;
	
	
	public long getCodCommunicatingEntity() {
		return codCommunicatingEntity;
	}
	public void setCodCommunicatingEntity(long codCommunicatingEntity) {
		this.codCommunicatingEntity = codCommunicatingEntity;
	}
	public String getBic() {
		return bic;
	}
	public void setBic(String bic) {
		this.bic = bic;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilePrefix() {
		return filePrefix;
	}
	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}
	public int getResponseDaysF1() {
		return responseDaysF1;
	}
	public void setResponseDaysF1(int responseDaysF1) {
		this.responseDaysF1 = responseDaysF1;
	}
	public int getResponseDaysF3() {
		return responseDaysF3;
	}
	public void setResponseDaysF3(int responseDaysF3) {
		this.responseDaysF3 = responseDaysF3;
	}
	public int getResponseDaysF6() {
		return responseDaysF6;
	}
	public void setResponseDaysF6(int responseDaysF6) {
		this.responseDaysF6 = responseDaysF6;
	}
}
